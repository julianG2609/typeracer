package conversion.bittransfer.serialisation.types;

import conversion.bittransfer.serialisation.Field;

class ShortField extends Field {
  ShortField(String name) {
    super(new boolean[16], 16, name, Short.class);
  }
}
