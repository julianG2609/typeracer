package transport.udp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.DatagramPacket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import transport.exceptions.TransportException;

public class UDPTest {

  @Test
  public void testUDP() throws TransportException {
    UDPTransporter server = new UDPTransporter(4448);
    UDPTransporter client = new UDPTransporter("localhost", 4448);
    byte[] msg = "Hello World".getBytes(StandardCharsets.UTF_8);
    client.send(msg);
    DatagramPacket rec = server.receive();
    assertEquals(
        Arrays.toString(msg), Arrays.toString(Arrays.copyOfRange(rec.getData(), 0, msg.length)));
  }
}
