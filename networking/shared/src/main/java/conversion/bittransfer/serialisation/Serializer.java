package conversion.bittransfer.serialisation;

import conversion.bittransfer.serialisation.types.Types;

/** Entry point for serialisation. */
public class Serializer {

  /** Default no arg constructor. */
  public Serializer() {}

  /**
   * Serialize the given object.
   *
   * @param obj the object
   * @return the serialized field
   */
  public Field serialize(Object obj) {
    return Types.from(obj).getConverter().serialize(obj, "");
  }

  /**
   * Serialize the given object and set the name to the field.
   *
   * @param obj the object
   * @param name the name of the field
   * @return the serialized field
   */
  public Field serialize(Object obj, String name) {
    return Types.from(obj).getConverter().serialize(obj, name);
  }

  /**
   * Deserialize the given field.
   *
   * @param field the field
   * @return the deserialized object
   */
  public Object deserialize(Field field) {
    return Types.from(field).getConverter().deserialize(field);
  }
}
