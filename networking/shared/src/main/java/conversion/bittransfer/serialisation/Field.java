package conversion.bittransfer.serialisation;

import java.util.Arrays;

/** Represents one object converted for transport over network. */
public abstract class Field {
  boolean[] content;
  String name;
  int staticSize;
  Class<?> clazz;

  /**
   * Internal constructor for the Field.
   *
   * <p>Used for serialization
   *
   * @param content the boolean array representation of the field
   * @param staticSize the static size of the field. 0 if dynamic field.
   * @param name the variable name of the field
   * @param clazz the class of the field
   */
  protected Field(boolean[] content, int staticSize, String name, Class<?> clazz) {
    this.content = content.clone();
    this.name = name;
    this.staticSize = staticSize;
    this.clazz = clazz;
  }

  /**
   * Getter for the boolean array representation of the object.
   *
   * @return the boolean array
   */
  public boolean[] getContent() {
    return content.clone();
  }

  /**
   * Setter for the boolean array representation of the object.
   *
   * @param copy the boolean array to set
   */
  public void setContent(boolean[] copy) {
    if (staticSize != 0) {
      if (staticSize >= 0) System.arraycopy(copy, 0, content, 0, staticSize);
    } else {
      content = copy.clone();
    }
  }

  /**
   * Getter for the name of the field.
   *
   * @return the name of the field
   */
  public String getName() {
    return name;
  }

  /**
   * Getter for the clazz of the field.
   *
   * <p>Used for initialisation of received objects.
   *
   * @return the clazz of the field
   */
  public Class<?> getClazz() {
    return clazz;
  }

  @Override
  public String toString() {
    return "name: "
        + name
        + ", staticSize: "
        + staticSize
        + ", content: "
        + Arrays.toString(content);
  }
}
