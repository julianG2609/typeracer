package transport;

import java.net.InetSocketAddress;
import transport.exceptions.TransportException;

/** Abstract class for the Transport layer. */
public abstract class Transporter {

  private final InetSocketAddress address;

  /**
   * Internal constructor for subclasses.
   *
   * @param address the connected address
   */
  protected Transporter(InetSocketAddress address) {
    this.address = address;
  }

  /**
   * Sends the specified byte array to the set address.
   *
   * @param msg the message to send
   * @throws TransportException throws on IO exception {@link TransportException}
   */
  public abstract void send(byte[] msg) throws TransportException;

  /**
   * Receives the specified number of bytes. Blocks till the specified number of bytes is available.
   * If end of stream is reached, a smaller byte array is returned. All consecutive calls will
   * return an empty array.
   *
   * @param size the number of bytes to receive
   * @return the received byte array
   * @throws TransportException throws on IO Exception stream closed {@link TransportException}
   */
  public abstract byte[] receive(int size) throws TransportException;

  /**
   * Receives one byte. Blocks if no data is available.
   *
   * @return the received byte
   * @throws TransportException throws on IO Exception and stream closed {@link TransportException}
   */
  public abstract byte receive() throws TransportException;

  /**
   * Initializes the connection to the specified address.
   *
   * @throws TransportException on IOException, SecurityException and invalid port number {@link
   *     TransportException}
   */
  public abstract void init() throws TransportException;

  /**
   * Closes the connection of this transporter.
   *
   * @throws TransportException on IOException
   */
  public abstract void close() throws TransportException;

  /**
   * Getter for the connected address.
   *
   * @return the connected address
   */
  protected InetSocketAddress getAddress() {
    return address;
  }
}
