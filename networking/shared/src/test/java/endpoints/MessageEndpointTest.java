package endpoints;

import static org.junit.jupiter.api.Assertions.assertEquals;

import conversion.json.JSONConverter;
import endpoints.messaging.MessageEndpoint;
import endpoints.messaging.MessageHandle;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;
import transport.exceptions.TransportException;

public class MessageEndpointTest {

  @Test
  public void testMessageEndpoint() throws IOException, TransportException {
    AtomicBoolean checked = new AtomicBoolean(false);

    ServerSocket serverSocket = new ServerSocket(4447);
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
    EndpointManager client = new EndpointManager(new JSONConverter(), "localhost", 4447);
    client.connect();
    MessageEndpoint serverEndpoint = new MessageEndpoint();
    while (server.get() == null) {
      Thread.onSpinWait();
    }
    server.get().registerEndpoint(serverEndpoint);
    MessageEndpoint clientEndpoint = new MessageEndpoint();
    client.registerEndpoint(clientEndpoint);
    MessageHandle<TestMessage> serverHandle =
        new MessageHandle<>(
            TestMessage.class,
            () -> new TestMessage(""),
            (message) -> {
              System.out.println(message.message);
              assertEquals("This is a test message.", message.message);
              checked.set(true);
            });
    serverEndpoint.registerHandle(serverHandle);
    MessageHandle<TestMessage> clientHandle =
        new MessageHandle<>(
            TestMessage.class,
            () -> new TestMessage(""),
            (message) -> System.out.println(message.message));
    clientEndpoint.registerHandle(clientHandle);
    clientHandle.send(new TestMessage("This is a test message."));
    while (!checked.get()) {
      Thread.onSpinWait();
    }
  }

  private record TestMessage(String message) {}
}
