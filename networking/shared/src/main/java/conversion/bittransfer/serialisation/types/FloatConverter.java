package conversion.bittransfer.serialisation.types;

import conversion.bittransfer.serialisation.Converter;
import conversion.bittransfer.serialisation.Field;

class FloatConverter extends Converter<Float> {

  @Override
  protected Field serialize(Float in, String name) {
    int inf = Float.floatToIntBits(in);
    FloatField field = new FloatField(name);
    boolean[] content = new boolean[32];
    for (int i = 0; i < 32; i++) {
      content[i] = ((inf >> i) & 1) == 1;
    }
    field.setContent(content);
    return field;
  }

  @Override
  protected Float deserialize(Field in) {
    int out = 0;
    for (int i = 0; i < 32; i++) {
      if (in.getContent()[i]) {
        out ^= 1 << i;
      }
    }
    return Float.intBitsToFloat(out);
  }
}
