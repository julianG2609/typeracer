package endpoints;

/** Enum to register all possible packet types with their id. */
public enum MessageType {
  /** Endpoint Packet. */
  Endpoint((byte) 0b00000000),
  /** Endpoint Sync Packet. */
  EndpointSync((byte) 0b00000001),
  ;

  MessageType(byte type) {
    this.type = type;
  }

  /** The message id. */
  public final byte type;

  /**
   * Gets the Message type via its id.
   *
   * @param type the id
   * @return the Message type
   */
  public static MessageType valueOf(byte type) {
    for (MessageType mt : MessageType.values()) {
      if (mt.type == type) {
        return mt;
      }
    }
    return null;
  }
}
