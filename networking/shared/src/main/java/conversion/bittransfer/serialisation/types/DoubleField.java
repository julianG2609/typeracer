package conversion.bittransfer.serialisation.types;

import conversion.bittransfer.serialisation.Field;

class DoubleField extends Field {
  DoubleField(String name) {
    super(new boolean[64], 64, name, Double.class);
  }
}
