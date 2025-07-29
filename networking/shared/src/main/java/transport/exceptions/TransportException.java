package transport.exceptions;

/** Class to group all possible exceptions in Transporter implementations. */
public abstract class TransportException extends Exception {

  /**
   * Constructor setting a message for the exception.
   *
   * @param message the exception message
   */
  public TransportException(String message) {
    super(message);
  }

  /**
   * Constructor setting an additional cause to the exception.
   *
   * @param message the exception message
   * @param cause the exception cause
   */
  public TransportException(String message, Throwable cause) {
    super(message, cause);
  }
}
