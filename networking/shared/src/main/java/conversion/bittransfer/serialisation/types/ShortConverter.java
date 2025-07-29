package conversion.bittransfer.serialisation.types;

import conversion.bittransfer.serialisation.Converter;
import conversion.bittransfer.serialisation.Field;

class ShortConverter extends Converter<Short> {

  @Override
  protected Field serialize(Short in, String name) {
    ShortField field = new ShortField(name);
    boolean[] content = new boolean[16];
    for (int i = 0; i < 16; i++) {
      content[i] = ((in >> i) & 1) == 1;
    }
    field.setContent(content);
    return field;
  }

  @Override
  protected Short deserialize(Field in) {
    short out = 0;
    for (int i = 0; i < 16; i++) {
      if (in.getContent()[i]) {
        out ^= (short) (1 << i);
      }
    }
    return out;
  }
}
