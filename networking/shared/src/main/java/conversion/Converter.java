package conversion;

/** Abstraction layer for all type converters. */
public abstract class Converter {

  /** Used by subclasses only. */
  public Converter() {}

  /**
   * Converts the given object into its networking compatible representation.
   *
   * @param obj the Object to convert
   * @return the network representation
   */
  public abstract byte[] convert(Object obj);

  /**
   * Converts the networking representation back into the original Object.
   *
   * @param obj the network representation
   * @param type an object compatible with the representation
   * @return the object
   * @param <T> the type of the represented data
   */
  public abstract <T> T convert(byte[] obj, T type);
}
