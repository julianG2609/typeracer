package endpoints.packet;

import endpoints.MessageType;
import java.util.Arrays;

/** Represents a packet of data to be exchanged between endpoints. */
public class EndpointPacket extends Packet {
  private EndpointPacket(byte[] bytes) {
    super(bytes);
  }

  /**
   * Getter for the endpoints id
   *
   * @return the endpoint id
   */
  public byte getEndpointId() {
    return endpointId;
  }

  /**
   * Getter for the use data of this packet. Excludes header.
   *
   * @return the data
   */
  public byte[] getPayload() {
    return payload.clone();
  }

  private byte endpointId;
  private byte[] payload;

  /**
   * Construct a EndpointPacket via an id and data.
   *
   * @param endpointId the endpoints id
   * @param payload the data to be sent
   * @return the EndpointPacket
   */
  public static EndpointPacket valueOf(byte endpointId, byte[] payload) {
    byte[] packet = wrap(payload.length + 2);
    packet[4] = MessageType.Endpoint.type;
    packet[5] = endpointId;
    System.arraycopy(payload, 0, packet, 6, payload.length);
    EndpointPacket endpointPacket = new EndpointPacket(packet);
    endpointPacket.endpointId = endpointId;
    endpointPacket.payload = payload;
    return endpointPacket;
  }

  /**
   * Reconstructs an EndpointPacket via the received data.
   *
   * @param packet the received data
   * @return the EndpointPacket
   */
  public static EndpointPacket valueOf(byte[] packet) {
    EndpointPacket endpointPacket = new EndpointPacket(packet);
    endpointPacket.endpointId = packet[0];
    endpointPacket.payload = Arrays.copyOfRange(packet, 1, packet.length);
    return endpointPacket;
  }
}
