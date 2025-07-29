package transport.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import transport.Transporter;
import transport.exceptions.*;

/** Transport layer implementation for TCP Protocol. */
public class TCPTransporter extends Transporter {

  /** The used socket. */
  public Socket socket;

  private InputStream inputStream;
  private OutputStream outputStream;

  /**
   * Constructor to create a new connection to the specifies address.
   *
   * @param address the address to connect to
   */
  public TCPTransporter(InetSocketAddress address) {
    super(address);
  }

  /**
   * Constructor to wrap an already connected socket.
   *
   * @param socket the socket to use. Has to be connected already.
   */
  public TCPTransporter(Socket socket) {
    super((InetSocketAddress) socket.getRemoteSocketAddress());
    this.socket = socket;
  }

  @Override
  public void send(byte[] msg) throws TransportException {
    try {
      outputStream.write(msg);
      outputStream.flush();
    } catch (IOException e) {
      throw new TransportIOException("IOException during send.", e);
    }
  }

  @Override
  public byte[] receive(int size) throws TransportException {
    try {
      byte[] buffer = inputStream.readNBytes(size);
      if (buffer.length == 0) {
        throw new TransportIOException("End of stream reached.", null);
      }
      return buffer;
    } catch (IOException e) {
      throw new TransportIOException("IOException during receive.", e);
    }
  }

  @Override
  public byte receive() throws TransportException {
    try {
      byte buffer = (byte) inputStream.read();
      if (buffer == -1) {
        throw new TransportIOException("End of stream reached.", null);
      }
      return buffer;
    } catch (IOException e) {
      throw new TransportIOException("IOException during receive.", e);
    }
  }

  @Override
  public void init() throws TransportException {
    try {
      if (socket == null) {
        socket = new Socket(getAddress().getAddress(), getAddress().getPort());
      }
      inputStream = socket.getInputStream();
      outputStream = socket.getOutputStream();
    } catch (IOException e) {
      throw new TransportIOException("IOException during initialisation.", e);
    } catch (IllegalArgumentException e) {
      throw new TransportPortException(getAddress().getPort());
    } catch (SecurityException e) {
      throw new TransportSecurityException(
          "SecurityException during initialisation. Check your SecurityManager configuration.", e);
    }
  }

  @Override
  public void close() throws TransportIOException {
    try {
      socket.close();
    } catch (IOException e) {
      throw new TransportIOException("Error while closing", e);
    }
  }
}
