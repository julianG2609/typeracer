package endpoints;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import conversion.json.JSONConverter;
import endpoints.synchronisation.SynchronisationEndpoint;
import endpoints.synchronisation.SynchronizedId;
import endpoints.synchronisation.Updatable;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;
import transport.exceptions.TransportException;

public class SynchronisationEndpointTest {

  private static boolean check = false;

  private static void check() {
    check = true;
  }

  @Test
  public void testSynchronisationEndpoint()
      throws IOException, InterruptedException, TransportException {

    ServerSocket serverSocket = new ServerSocket(4446);
    AtomicReference<EndpointManager> server = new AtomicReference<>();
    Executor executor = Executors.newSingleThreadExecutor();
    executor.execute(
        () -> {
          try {
            server.set(new EndpointManager(new JSONConverter(), serverSocket.accept()));
            server.get().connect();
          } catch (IOException | TransportException e) {
            throw new RuntimeException(e);
          }
        });
    EndpointManager client = new EndpointManager(new JSONConverter(), "localhost", 4446);
    client.connect();
    SynchronisationEndpoint serverEndpoint = new SynchronisationEndpoint();
    while (server.get() == null) {
      Thread.onSpinWait();
    }
    server.get().registerEndpoint(serverEndpoint);
    SynchronisationEndpoint clientEndpoint = new SynchronisationEndpoint();
    client.registerEndpoint(clientEndpoint);
    TestClass clientClass = new TestClass("", 1);
    clientEndpoint.register(clientClass);
    TestClass serverClass = new TestClass("lalala", 1);
    serverEndpoint.register(serverClass);
    clientClass.setMessage("Test message");
    clientEndpoint.synchronize(clientClass);
    Thread.sleep(100);
    assertEquals("Test message", clientClass.getMessage());
    assertEquals("Test message", serverClass.getMessage());
    assertEquals(1, serverClass.getId());
    serverClass.setMessage("Test message1!");
    serverEndpoint.synchronize(serverClass);
    Thread.sleep(100);
    assertEquals("Test message1!", clientClass.getMessage());
    assertEquals("Test message1!", serverClass.getMessage());
    assertEquals(1, clientClass.getId());

    TestClass clientClass2 = new TestClass("", 2);
    clientEndpoint.register(clientClass2);
    TestClass serverClass2 = new TestClass("lalala", 2);
    serverEndpoint.register(serverClass2);
    Thread.sleep(100);
    clientClass2.setMessage("Test message2");
    clientEndpoint.synchronize(clientClass2);
    Thread.sleep(100);
    assertEquals("Test message2", clientClass2.getMessage());
    assertEquals("Test message2", serverClass2.getMessage());
    assertEquals(2, serverClass2.getId());
    serverClass2.setMessage("Test message1");
    serverEndpoint.synchronize(serverClass2);
    Thread.sleep(100);
    assertEquals("Test message1", clientClass2.getMessage());
    assertEquals("Test message1", serverClass2.getMessage());
    assertEquals(2, clientClass2.getId());

    assertEquals("Test message1!", clientClass.getMessage());
    assertEquals("Test message1!", serverClass.getMessage());

    assertTrue(check);
  }

  private class TestClass implements Updatable {
    private String message;
    @SynchronizedId private transient int id;

    public TestClass(String message, int id) {
      this.message = message;
      this.id = id;
    }

    public TestClass() {}

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }

    public int getId() {
      return id;
    }

    @Override
    public void update() {
      check();
    }
  }
}
