package conversion.bittransfer.serialisation.types;

import conversion.bittransfer.serialisation.Field;

class LongField extends Field {
  LongField(String name) {
    super(new boolean[64], 64, name, Long.class);
  }
}
