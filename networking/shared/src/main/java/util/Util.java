package util;

/** Utility methods for the networking. */
public class Util {

  private Util() {}

  /**
   * Converts the given integer to byte array.
   *
   * @param value the int value
   * @return the byte array
   */
  public static byte[] convertIntToByteArray(int value) {
    return new byte[] {
      (byte) (value >> 24), (byte) (value >> 16), (byte) (value >> 8), (byte) value
    };
  }

  /**
   * Converts the given byte array to int.
   *
   * @param bytes the byte array
   * @return the int
   */
  public static int convertByteArrayToInt(byte[] bytes) {
    return ((bytes[0] & 0xFF) << 24)
        | ((bytes[1] & 0xFF) << 16)
        | ((bytes[2] & 0xFF) << 8)
        | ((bytes[3] & 0xFF));
  }
}
