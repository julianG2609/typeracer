package conversion.bittransfer.serialisation.types;

import conversion.bittransfer.serialisation.Field;
import conversion.bittransfer.serialisation.ParentField;

class SerializableField extends ParentField {
  SerializableField(String name, Field[] fields, Class<?> clazz) {
    super(name, fields, clazz);
  }
}
