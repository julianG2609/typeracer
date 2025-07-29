package conversion.bittransfer.serialisation.types;

import conversion.bittransfer.serialisation.Converter;
import conversion.bittransfer.serialisation.Field;

class DoubleConverter extends Converter<Double> {

  @Override
  protected Field serialize(Double in, String name) {
    long inf = Double.doubleToLongBits(in);
    DoubleField field = new DoubleField(name);
    boolean[] content = new boolean[64];
    for (int i = 0; i < 64; i++) {
      content[i] = ((inf >> i) & 1) == 1;
    }
    field.setContent(content);
    return field;
  }

  @Override
  protected Double deserialize(Field in) {
    long out = 0;
    for (int i = 0; i < 64; i++) {
      if (in.getContent()[i]) {
        out ^= 1L << i;
      }
    }
    return Double.longBitsToDouble(out);
  }
}
