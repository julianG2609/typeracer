package conversion.bittransfer.serialisation.types;

import conversion.bittransfer.serialisation.Converter;
import conversion.bittransfer.serialisation.Field;

class ByteConverter extends Converter<Byte> {

  @Override
  protected Field serialize(Byte in, String name) {
    ByteField field = new ByteField(name);
    boolean[] content = new boolean[8];
    for (int i = 0; i < 8; i++) {
      content[i] = ((in >> i) & 1) == 1;
    }
    field.setContent(content);
    return field;
  }

  @Override
  protected Byte deserialize(Field in) {
    byte out = 0;
    for (int i = 0; i < 8; i++) {
      if (in.getContent()[i]) {
        out ^= (byte) (1 << i);
      }
    }
    return out;
  }
}
