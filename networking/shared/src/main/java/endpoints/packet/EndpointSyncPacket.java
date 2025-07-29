package endpoints.packet;

import static util.Util.convertByteArrayToInt;
import static util.Util.convertIntToByteArray;

import endpoints.MessageType;
import java.util.Arrays;

/** Represents an id assignment for a new Endpoint. */
public class EndpointSyncPacket extends Packet {
  private EndpointSyncPacket(byte[] bytes) {
    super(bytes);
  }

  /**
   * Getter for the unique identifier.
   *
   * @return the UID
   */
  public int getUid() {
    return uid;
  }

  /**
   * Getter for the identifier.
   *
   * @return the ID
   */
  public byte getId() {
    return id;
  }

  int uid;
  byte id;

  /**
   * Constructs a SyncPacket from the uid and id.
   *
   * @param uid the UID
   * @param id the ID
   * @return the endpoint sync packet
   */
  public static EndpointSyncPacket valueOf(int uid, byte id) {
    byte[] packet = wrap(6);
    packet[4] = MessageType.EndpointSync.type;
    packet[5] = id;
    byte[] uidBytes = convertIntToByteArray(uid);
    System.arraycopy(uidBytes, 0, packet, 6, uidBytes.length);
    EndpointSyncPacket res = new EndpointSyncPacket(packet);
    res.uid = uid;
    res.id = id;
    return res;
  }

  /**
   * Reconstructs an endpoint sync packet from received bytes.
   *
   * @param bytes the received bytes
   * @return the endpoint sync packet
   */
  public static EndpointSyncPacket valueOf(byte[] bytes) {
    EndpointSyncPacket res = new EndpointSyncPacket(bytes);
    byte[] uidBytes = Arrays.copyOfRange(bytes, 1, bytes.length);
    res.uid = convertByteArrayToInt(uidBytes);
    res.id = bytes[0];
    return res;
  }

  @Override
  public int hashCode() {
    return uid;
  }
}
