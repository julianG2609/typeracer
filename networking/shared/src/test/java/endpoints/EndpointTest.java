package endpoints;

import conversion.json.JSONConverter;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;
import transport.exceptions.TransportException;

public class EndpointTest {

  @Test
  public void testDummyEndpoint() throws IOException, InterruptedException, TransportException {
    ServerSocket serverSocket = new ServerSocket(4445);
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
    EndpointManager client = new EndpointManager(new JSONConverter(), "localhost", 4445);
    client.connect();
    DummyEndpoint clientEndpoint = new DummyEndpoint();
    client.registerEndpoint(clientEndpoint);
    while (server.get() == null) {
      Thread.onSpinWait();
    }
    DummyEndpoint serverEndpoint = new DummyEndpoint();
    server.get().registerEndpoint(serverEndpoint);
    clientEndpoint.send("TestMessage");
  }
}
