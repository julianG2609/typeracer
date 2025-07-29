package conversion.bittransfer.serialisation;

/**
 * Converter for type T.
 *
 * <p>Each subclass converts a specified Type from and to arrays of boolean.
 *
 * @param <T> Type of the converted objects.
 */
public abstract class Converter<T> {

  /**
   * Default no arg constructor.
   *
   * <p>Used only by subclasses.
   */
  protected Converter() {}

  /**
   * Converts the given Object to Field and sets the name.
   *
   * @param in the object to convert
   * @param name the name of the variable
   * @return the equivalent boolean array
   */
  protected abstract Field serialize(T in, String name);

  /**
   * Converts the Field to the original object.
   *
   * @param in the field to convert
   * @return the object
   */
  protected abstract T deserialize(Field in);
}
