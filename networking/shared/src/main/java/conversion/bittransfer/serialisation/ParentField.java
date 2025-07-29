package conversion.bittransfer.serialisation;

import java.util.Arrays;

/** Extension on field for dynamic fields that have sub elements. */
public abstract class ParentField extends Field {
  Field[] children;

  /**
   * Internal constructor used for serialization.
   *
   * @param name name of the field
   * @param fields the sub-fields
   * @param clazz the clazz of the field
   */
  protected ParentField(String name, Field[] fields, Class<?> clazz) {
    super(new boolean[0], 0, name, clazz);
    children = fields.clone();
  }

  /**
   * Getter for all sub-fields.
   *
   * @return the sub-fields
   */
  public Field[] getChildren() {
    return children.clone();
  }

  @Override
  public String toString() {
    return "name: "
        + name
        + ", staticSize: "
        + staticSize
        + ", content: "
        + Arrays.toString(content)
        + ", children: "
        + Arrays.toString(children);
  }
}
