package conversion.bittransfer.serialisation.types;

import conversion.bittransfer.serialisation.Field;

class ByteField extends Field {
  ByteField(String name) {
    super(new boolean[8], 8, name, Byte.class);
  }
}
