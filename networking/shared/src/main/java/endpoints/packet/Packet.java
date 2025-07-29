package endpoints.packet;

import static util.Util.convertIntToByteArray;

/** Represents a packet of data to be sent by an endpoint manager. */
public abstract class Packet {

  /**
   * Default constructor taking the data.
   *
   * @param data the data
   */
  protected Packet(byte[] data) {
    this.data = data;
  }

  private final byte[] data;

  /**
   * Getter for the data including the header.
   *
   * @return the data
   */
  public byte[] getData() {
    return data.clone();
  }

  /**
   * Creates a new byte array with the specified size+1 and appends the total size to the front.
   *
   * @param size the size of the data, excluding the length field
   * @return a data array with just the length in it
   */
  protected static byte[] wrap(int size) {
    byte[] result = new byte[size + 4];
    byte[] lengthBytes = convertIntToByteArray(result.length);
    System.arraycopy(lengthBytes, 0, result, 0, lengthBytes.length);
    return result;
  }
}
