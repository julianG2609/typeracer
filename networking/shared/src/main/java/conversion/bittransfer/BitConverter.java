package conversion.bittransfer;

import conversion.Converter;

/** Future class to use th BitTransfer layer. Currently on ice because of prioritisation. */
public class BitConverter extends Converter {

  private BitConverter() {}

  @Override
  public byte[] convert(Object obj) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public <T> T convert(byte[] obj, T type) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
