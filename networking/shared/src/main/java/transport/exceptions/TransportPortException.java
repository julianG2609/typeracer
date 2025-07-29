package transport.exceptions;

/** Represents an error because of an invalid Port. */
public class TransportPortException extends TransportException {

  /**
   * Default constructor.
   *
   * @param port the invalid port
   */
  public TransportPortException(int port) {
    super("Invalid port: " + port);
  }
}
