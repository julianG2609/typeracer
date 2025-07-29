package conversion.bittransfer.serialisation.types;

import conversion.bittransfer.serialisation.Field;

class StringField extends Field {
  StringField(String name) {
    super(new boolean[0], 0, name, String.class);
  }
}
