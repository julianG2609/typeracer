package conversion.bittransfer.serialisation.types;

import conversion.bittransfer.serialisation.Converter;
import conversion.bittransfer.serialisation.Field;

class BoolConverter extends Converter<Boolean> {

  @Override
  protected Field serialize(Boolean in, String name) {
    return new BoolField(in, name);
  }

  @Override
  protected Boolean deserialize(Field in) {
    return in.getContent()[0];
  }
}
