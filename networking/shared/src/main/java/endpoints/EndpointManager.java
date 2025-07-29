package endpoints;

import static util.Util.convertByteArrayToInt;

import conversion.Converter;
import endpoints.packet.EndpointPacket;
import endpoints.packet.EndpointSyncPacket;
import endpoints.packet.Packet;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import transport.exceptions.TransportException;
import transport.tcp.TCPTransporter;

/**
 * Allows to connect multiple endpoints via one connected TCP port. Works similar to a reverse
 * proxy.
 */
public class EndpointManager {

  private static final int SEND_QUEUE_SIZE = 25;

  private final TCPTransporter transporter;

  /** The Converter. */
  Converter converter;

  private final Receiver receiver = new Receiver();
  private final Map<Byte, List<byte[]>> waitingPackets = new HashMap<>();

  private final Sender sender = new Sender();

  private final Random random = new Random();

  /** The Send queue. */
  BlockingQueue<Packet> sendQueue = new ArrayBlockingQueue<>(SEND_QUEUE_SIZE);

  /**
   * Constructs an endpoint manager via host and port.
   *
   * @param converter the default constructor
   * @param host the host to connect to
   * @param port the port to connect to
   */
  public EndpointManager(Converter converter, String host, int port) {
    this.converter = converter;
    transporter = new TCPTransporter(new InetSocketAddress(host, port));
    high = false;
  }

  /**
   * Constructs an endpoint manager from an already connected socket.
   *
   * @param converter the default converter
   * @param socket the connected socket
   */
  public EndpointManager(Converter converter, Socket socket) {
    this.converter = converter;
    transporter = new TCPTransporter(socket);
    high = true;
  }

  /**
   * Getter for the local address.
   *
   * @return the local address
   */
  public InetSocketAddress getLocalAddress() {
    return (InetSocketAddress) transporter.socket.getRemoteSocketAddress();
  }

  /**
   * Connect.
   *
   * @throws TransportException the transport exception
   */
  public void connect() throws TransportException {
    transporter.init();
    receiver.setDaemon(true);
    sender.setDaemon(true);
    receiver.start();
    sender.start();
  }

  private Runnable onDestroy;

  /**
   * Closes the connection. Should be called only if the connection is no longer needed. Frees up
   * the port. Nothing will work after this method is called.
   */
  public void destroy() {
    if (onDestroy != null) {
      onDestroy.run();
    }
    synchronized (endpoints) {
      endpoints.forEach(
          (id, endpoint) -> {
            endpoint.destroy();
          });
    }
    sender.interrupt();
    try {
      transporter.close();
    } catch (TransportException e) {
      sender.interrupt();
      receiver.interrupt();
    }
  }

  private byte idCounter = 0;

  private synchronized byte nextId() {
    return idCounter++;
  }

  private final Map<Byte, Endpoint> endpoints = new HashMap<>();

  private final Map<Integer, Byte> received = new HashMap<>();
  private final Map<Integer, Endpoint> pending = new HashMap<>();

  private final Boolean high;

  /**
   * Getter for the high property. Used for protocols that need a leader for the connection. Only
   * one of the connected endpoint managers will have this return true, the other will return false.
   *
   * @return the high property
   */
  public boolean getHigh() {
    return high;
  }

  /**
   * Registers a new endpoint to this connection and manager.
   *
   * @param endpoint the endpoint to register
   */
  public void registerEndpoint(Endpoint endpoint) {
    int uid = endpoint.getClass().getSimpleName().hashCode();
    if (high) {
      try {
        byte newId = nextId();
        sendQueue.put(EndpointSyncPacket.valueOf(uid, newId));
        registerEndpoint(newId, endpoint);
      } catch (InterruptedException ignored) {
        // Just quit.
      }
    } else {
      synchronized (received) {
        if (received.containsKey(uid)) {
          byte id = received.get(uid);
          registerEndpoint(id, endpoint);
          received.remove(uid);
        } else {
          synchronized (pending) {
            pending.put(uid, endpoint);
          }
        }
      }
    }
  }

  // Not really useful.
  /*public void unregisterEndpoint(Endpoint endpoint) {
    endpoints.values().remove(endpoint);
  }*/

  /**
   * Sends data to the endpoint with the specified id.
   *
   * @param endpoint the endpoint id
   * @param data the data to send
   */
  public void send(byte endpoint, byte[] data) {
    try {
      sendQueue.put(EndpointPacket.valueOf(endpoint, data));
    } catch (InterruptedException ignored) {
      // Just quit.
    }
  }

  private void registerEndpoint(byte id, Endpoint endpoint) {
    synchronized (endpoints) {
      endpoints.put(id, endpoint);
      endpoint.id = id;
      endpoint.converter = converter;
      endpoint.endpointManager = this;
      endpoint.registeredHandler();
      synchronized (waitingPackets) {
        if (waitingPackets.containsKey(id)) {
          for (byte[] packet : waitingPackets.get(id)) {
            endpoint.handleReceived(packet);
          }
          waitingPackets.remove(id);
        }
      }
    }
  }

  /**
   * Sets on destroy.
   *
   * @param onDestroy the on destroy
   */
  public void setOnDestroy(Runnable onDestroy) {
    this.onDestroy = onDestroy;
  }

  private class Receiver extends Thread {

    private void handleEndpointPacket(EndpointPacket packet) {
      synchronized (endpoints) {
        Endpoint endpoint = endpoints.get(packet.getEndpointId());
        if (endpoint == null) {
          synchronized (waitingPackets) {
            if (waitingPackets.containsKey(packet.getEndpointId())) {
              waitingPackets.get(packet.getEndpointId()).add(packet.getPayload());
            } else {
              waitingPackets.put(packet.getEndpointId(), List.of(packet.getPayload()));
            }
          }
        } else {
          endpoint.handleReceived(packet.getPayload());
        }
      }
    }

    private void handleSyncPacket(EndpointSyncPacket packet) {

      synchronized (pending) {
        if (pending.containsKey(packet.getUid())) {
          Endpoint endpoint = pending.get(packet.getUid());
          registerEndpoint(packet.getId(), endpoint);
          pending.remove(packet.getUid());
        } else {
          synchronized (received) {
            received.put(packet.getUid(), packet.getId());
          }
        }
      }
    }

    @Override
    public void run() {
      interrupted();
      try {
        while (true) {
          byte[] lengthBytes = transporter.receive(4);
          int length = convertByteArrayToInt(lengthBytes);
          byte type = transporter.receive();
          byte[] payload = transporter.receive(length - 5);
          MessageType messageType = MessageType.valueOf(type);
          switch (messageType) {
            case MessageType.Endpoint -> handleEndpointPacket(EndpointPacket.valueOf(payload));
            case MessageType.EndpointSync -> handleSyncPacket(EndpointSyncPacket.valueOf(payload));
            case null -> {
              throw new RuntimeException("Unexpected null packet");
            }
          }
        }
      } catch (TransportException ignored) {
        destroy();
      }
    }
  }

  private class Sender extends Thread {
    @Override
    public void run() {
      try {
        while (true) {
          byte[] packet = sendQueue.take().getData();
          System.out.println(Arrays.toString(packet));
          transporter.send(packet);
        }
      } catch (TransportException e) {
        destroy();
      } catch (InterruptedException ignored) {
        // Just quit.
      }
    }
  }
}
