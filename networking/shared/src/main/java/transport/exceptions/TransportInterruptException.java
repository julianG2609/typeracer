package transport.exceptions;

/** Exception to be thrown when a blocking IO operation could not complete due to an interrupt. */
public class TransportInterruptException extends TransportException {
  /**
   * Default constructor.
   *
   * @param message the exception message
   */
  public TransportInterruptException(String message) {
    super(message);
  }
}
