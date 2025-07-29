package conversion.bittransfer.serialisation.types;

import conversion.bittransfer.serialisation.Converter;
import conversion.bittransfer.serialisation.Field;

class LongConverter extends Converter<Long> {

  @Override
  protected Field serialize(Long in, String name) {
    LongField field = new LongField(name);
    boolean[] content = new boolean[64];
    for (int i = 0; i < 64; i++) {
      content[i] = ((in >> i) & 1) == 1;
    }
    field.setContent(content);
    return field;
  }

  @Override
  protected Long deserialize(Field in) {
    long out = 0;
    for (int i = 0; i < 64; i++) {
      if (in.getContent()[i]) {
        out ^= 1L << i;
      }
    }
    return out;
  }
}
