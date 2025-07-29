package endpoints.messaging;

import static util.Util.convertByteArrayToInt;
import static util.Util.convertIntToByteArray;

import endpoints.Endpoint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * An Endpoint to send and receive simple messages as classes. No guaranty concerning time or order
 * of messages. Guarantied transmission and reception.
 */
public class MessageEndpoint extends Endpoint {

  /** Default constructor. */
  public MessageEndpoint() {}

  private final BlockingQueue<Message<Object>> sendQueue = new ArrayBlockingQueue<>(20);

  private final Map<Integer, MessageHandle<Object>> handles = new HashMap<>();

  private final Map<Integer, List<byte[]>> receiveQueue = new HashMap<>();

  private final Sender sender = new Sender();

  @Override
  protected void handleReceived(byte[] data) {
    byte[] uidBytes = new byte[4];
    System.arraycopy(data, 0, uidBytes, 0, 4);
    byte[] messageBytes = new byte[data.length - 4];
    System.arraycopy(data, 4, messageBytes, 0, data.length - 4);
    int uid = convertByteArrayToInt(uidBytes);
    if (!handles.containsKey(uid)) {
      if (!receiveQueue.containsKey(uid)) {
        receiveQueue.put(uid, new ArrayList<>());
      }
      receiveQueue.get(uid).add(messageBytes);
      return;
    }
    MessageHandle<Object> handle = handles.get(uid);
    try {
      Object obj = handle.construct();
      Object message = converter.convert(messageBytes, obj);
      handle.handle(message);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void registeredHandler() {
    if (sender.isAlive()) return;
    sender.setDaemon(true);
    sender.start();
  }

  @Override
  public void destroy() {
    sender.interrupt();
  }

  /**
   * Register the specified handle to this endpoint. One handle is equivalent to one class that can
   * be sent.
   *
   * @param messageHandle the message handle
   * @param <T> the class to be sent and received
   */
  @SuppressWarnings("unchecked")
  public <T> void registerHandle(MessageHandle<T> messageHandle) {
    handles.put(messageHandle.getUid(), (MessageHandle<Object>) messageHandle);
    messageHandle.setEndpoint(this);
    if (receiveQueue.containsKey(messageHandle.getUid())) {
      for (byte[] receiveBytes : receiveQueue.get(messageHandle.getUid())) {
        try {
          T res = messageHandle.construct();
          res = converter.convert(receiveBytes, res);
          messageHandle.handle(res);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    }
  }

  /**
   * Removes the specified handler from this endpoint. Reverse operation to registerHandler.
   *
   * @param messageHandle the message handle
   */
  public void unregisterHandle(MessageHandle<?> messageHandle) {
    handles.remove(messageHandle.getUid());
    messageHandle.setEndpoint(null);
  }

  @SuppressWarnings("unchecked")
  <T> void sendMessage(MessageHandle<T> handle, T data) {
    try {
      sendQueue.put((Message<Object>) new Message<>(handle, data));
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private record Message<T>(MessageHandle<T> handle, T data) {}

  private class Sender extends Thread {
    @Override
    public void run() {
      interrupted();
      try {
        while (true) {
          Message<?> message = sendQueue.take();
          byte[] data = converter.convert(message.data);
          byte[] messageBytes = new byte[data.length + 4];
          byte[] uidBytes = convertIntToByteArray(message.handle.getUid());
          System.arraycopy(uidBytes, 0, messageBytes, 0, 4);
          System.arraycopy(data, 0, messageBytes, 4, data.length);
          send(messageBytes);
        }
      } catch (InterruptedException ignored) {

      }
    }
  }
}
