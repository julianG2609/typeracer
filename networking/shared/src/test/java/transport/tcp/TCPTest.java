package transport.tcp;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import transport.exceptions.TransportException;

public class TCPTest {

  private static ServerSocket server;
  private static Socket clientSocket;
  private static final Executor executor = Executors.newSingleThreadExecutor();
  private static Socket serverSocket;

  @BeforeAll
  public static void init() throws IOException {
    server = new ServerSocket(4444);
    executor.execute(
        () -> {
          try {
            serverSocket = server.accept();
          } catch (IOException e) {
            fail();
          }
        });
    clientSocket = new Socket("localhost", 4444);
  }

  @Test
  public void transferTest() throws TransportException {
    TCPTransporter client = new TCPTransporter(clientSocket);
    client.init();
    while (serverSocket == null) {
      Thread.onSpinWait();
    }
    TCPTransporter server = new TCPTransporter(serverSocket);
    server.init();
    byte[] buffer = new byte[1024];
    Arrays.fill(buffer, (byte) 122);
    client.send(buffer);
    byte[] rec = server.receive(buffer.length);
    for (int i = 0; i < rec.length; i++) {
      assertEquals(buffer[i], rec[i]);
    }
  }
}
