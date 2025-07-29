package conversion.bittransfer.serialisation.types;

import conversion.bittransfer.serialisation.Converter;
import conversion.bittransfer.serialisation.Field;
import java.nio.charset.StandardCharsets;

class StringConverter extends Converter<String> {

  @Override
  protected Field serialize(String in, String name) {
    byte[] bytes = in.getBytes(StandardCharsets.UTF_8);
    boolean[] content = new boolean[bytes.length * 8];
    for (int i = 0; i < bytes.length; i++) {
      for (int j = 0; j < 8; j++) {
        content[i * 8 + j] = ((bytes[i] >> j) & 1) == 1;
      }
    }
    StringField out = new StringField(name);
    out.setContent(content);
    return out;
  }

  @Override
  protected String deserialize(Field in) {
    if (in.getContent().length % 8 != 0) {
      throw new IllegalStateException("String content has to align on byte.");
    }
    byte[] bytes = new byte[in.getContent().length / 8];
    for (int i = 0; i < bytes.length; i++) {
      for (int j = 0; j < 8; j++) {
        if (in.getContent()[i * 8 + j]) {
          bytes[i] ^= (byte) (1 << j);
        }
      }
    }
    return new String(bytes, StandardCharsets.UTF_8);
  }
}
