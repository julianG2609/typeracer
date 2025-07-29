package endpoints.messaging;

import java.util.concurrent.Callable;

/**
 * A MessageHandle is a reference to send and receive a specific type of message. Should be
 * registered with a MessageEndpoint.
 *
 * @param <T> the type of message to handle
 */
public class MessageHandle<T> {

  private final Class<T> messageClass;
  private final Callable<T> constructor;
  private final MessageHandler<T> handler;

  private volatile MessageEndpoint endpoint;

  /**
   * Create a new message handle for a message class.
   *
   * @param messageClass the class of messages to handle
   * @param constructor a no-arg constructor for deserialization
   * @param handler the reception handler
   */
  public MessageHandle(Class<T> messageClass, Callable<T> constructor, MessageHandler<T> handler) {
    this.messageClass = messageClass;
    this.constructor = constructor;
    this.handler = handler;
    uid = messageClass.getSimpleName().hashCode();
  }

  private final int uid;

  int getUid() {
    return uid;
  }

  T construct() throws Exception {
    return constructor.call();
  }

  void handle(T t) {
    handler.handle(t);
  }

  /**
   * Sends the specified Message.
   *
   * @param message the message
   */
  public void send(T message) {
    while (endpoint == null) {
      Thread.onSpinWait();
    }
    endpoint.sendMessage(this, message);
  }

  void setEndpoint(MessageEndpoint endpoint) {
    this.endpoint = endpoint;
  }
}
