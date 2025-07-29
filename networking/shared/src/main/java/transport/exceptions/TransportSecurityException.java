package transport.exceptions;

/** Represents a security exception in the transporter layer. */
public class TransportSecurityException extends TransportException {
  /**
   * Default constructor.
   *
   * @param message the exception message
   * @param cause the exception cause
   */
  public TransportSecurityException(String message, Throwable cause) {
    super(message, cause);
  }
}
