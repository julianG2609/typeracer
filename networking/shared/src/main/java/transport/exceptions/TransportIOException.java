package transport.exceptions;

/** Represents a IOException in the transporter layer. */
public class TransportIOException extends TransportException {
  /**
   * Default constructor.
   *
   * @param message the exception message
   * @param cause the exception cause
   */
  public TransportIOException(String message, Throwable cause) {
    super(message, cause);
  }
}
