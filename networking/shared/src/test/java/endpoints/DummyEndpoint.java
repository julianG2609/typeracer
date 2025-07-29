package endpoints;

public class DummyEndpoint extends Endpoint {

  boolean registered = false;

  @Override
  protected void handleReceived(byte[] data) {
    System.out.println(converter.convert(data, ""));
  }

  @Override
  protected void registeredHandler() {
    registered = true;
  }

  @Override
  public void destroy() {}

  public void send(String message) throws InterruptedException {
    while (!registered) {
      Thread.sleep(100);
    }
    send(converter.convert(message));
  }
}
