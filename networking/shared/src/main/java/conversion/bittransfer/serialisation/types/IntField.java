package conversion.bittransfer.serialisation.types;

import conversion.bittransfer.serialisation.Field;

class IntField extends Field {
  IntField(String name) {
    super(new boolean[32], 32, name, Integer.class);
  }
}
