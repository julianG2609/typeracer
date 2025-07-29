package conversion.bittransfer.serialisation.types;

import conversion.bittransfer.serialisation.Field;

class FloatField extends Field {
  FloatField(String name) {
    super(new boolean[32], 32, name, Float.class);
  }
}
