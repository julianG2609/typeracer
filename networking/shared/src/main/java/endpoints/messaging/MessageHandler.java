package endpoints.messaging;

/**
 * Lambda interface to handle the reception of a message.
 *
 * @param <T> the message type
 */
public interface MessageHandler<T> {

  /**
   * Handle the reception of a message.
   *
   * @param t the received message.
   */
  void handle(T t);
}
