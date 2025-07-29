package conversion.bittransfer.serialisation.types;

import conversion.bittransfer.serialisation.Converter;
import conversion.bittransfer.serialisation.Field;

class IntConverter extends Converter<Integer> {

  @Override
  protected Field serialize(Integer in, String name) {
    IntField field = new IntField(name);
    boolean[] content = new boolean[32];
    for (int i = 0; i < 32; i++) {
      content[i] = ((in >> i) & 1) == 1;
    }
    field.setContent(content);
    return field;
  }

  @Override
  protected Integer deserialize(Field in) {
    int out = 0;
    for (int i = 0; i < 32; i++) {
      if (in.getContent()[i]) {
        out ^= 1 << i;
      }
    }
    return out;
  }
}
