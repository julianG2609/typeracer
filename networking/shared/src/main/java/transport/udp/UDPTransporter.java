package transport.udp;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import transport.exceptions.TransportException;
import transport.exceptions.TransportIOException;
import transport.exceptions.TransportInterruptException;

/** Basic transportation layer for UDP communication. */
public class UDPTransporter {

  private DatagramSocket socket;

  private BlockingQueue<DatagramPacket> send = new ArrayBlockingQueue<>(50);
  private BlockingQueue<DatagramPacket> receive = new ArrayBlockingQueue<>(50);

  private InetAddress address;
  private int port;

  private Receiver receiver = new Receiver();
  private Sender sender = new Sender();

  /**
   * Constructor for the client side.
   *
   * @param host the hostname of the server
   * @param port the port of the server
   */
  public UDPTransporter(String host, int port) {
    try {
      socket = new DatagramSocket();
    } catch (SocketException e) {
    }
    this.port = port;
    try {
      this.address = InetAddress.getByName(host);
    } catch (UnknownHostException e) {
    }
    start();
  }

  /**
   * Constructor for the server side.
   *
   * @param port the port to bind to
   */
  public UDPTransporter(int port) {
    try {
      socket = new DatagramSocket(port);
    } catch (SocketException e) {
    }
    start();
  }

  private void start() {
    receiver.start();
    sender.start();
  }

  /**
   * Sends the specified message to the server. Use only on client side.
   *
   * @param msg the message to send
   * @throws TransportException see {@link TransportException}
   */
  public void send(byte[] msg) throws TransportException {
    if (address == null) {
      throw new TransportIOException("Address must be set on server.", null);
    }
    DatagramPacket packet = new DatagramPacket(msg, msg.length, address, port);
    try {
      send.put(packet);
    } catch (InterruptedException e) {
      throw new TransportInterruptException("Could not send this packet.");
    }
  }

  /**
   * Sends the specified message to the specified maschine.
   *
   * @param msg the message to send
   * @param address the address to send to
   * @param port the port to send to
   * @throws TransportException see {@link TransportException}
   */
  public void send(byte[] msg, InetAddress address, int port) throws TransportException {
    DatagramPacket packet = new DatagramPacket(msg, msg.length, address, port);
    try {
      send.put(packet);
    } catch (InterruptedException e) {
      throw new TransportInterruptException("Could not send this packet.");
    }
  }

  /**
   * Receives one Datagram packet, blocking till one is available.
   *
   * @return the datagram packet
   * @throws TransportException see {@link TransportException}
   */
  public DatagramPacket receive() throws TransportException {
    try {
      return receive.take();
    } catch (InterruptedException e) {
      throw new TransportInterruptException("Could not receive this packet.");
    }
  }

  private class Sender extends Thread {
    @Override
    public void run() {
      try {
        while (true) {
          DatagramPacket packet = send.take();
          socket.send(packet);
        }
      } catch (InterruptedException | IOException ignored) {

      }
    }
  }

  private class Receiver extends Thread {
    @Override
    public void run() {
      try {
        while (true) {
          byte[] buf = new byte[512];
          DatagramPacket packet = new DatagramPacket(buf, buf.length);
          socket.receive(packet);
          receive.put(packet);
        }
      } catch (InterruptedException | IOException ignored) {

      }
    }
  }
}
