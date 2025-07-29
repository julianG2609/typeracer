package conversion.bittransfer.serialisation.types;

import conversion.bittransfer.serialisation.Field;

class BoolField extends Field {
  BoolField(boolean content, String name) {
    super(new boolean[] {content}, 1, name, Boolean.class);
  }
}
