package conversion.bittransfer.serialisation.types;

import conversion.bittransfer.serialisation.Converter;

/** Enum to keep track of all available type converters. */
public enum Types {
  /** Boolean types. */
  Boolean(obj -> obj instanceof Boolean || obj instanceof BoolField, new BoolConverter()),
  /** Integer types. */
  Integer(obj -> obj instanceof Integer || obj instanceof IntField, new IntConverter()),
  /** Short types. */
  Short(obj -> obj instanceof Short || obj instanceof ShortField, new ShortConverter()),
  /** Byte types. */
  Byte(obj -> obj instanceof Byte || obj instanceof ByteField, new ByteConverter()),
  /** Long types. */
  Long(obj -> obj instanceof Long || obj instanceof LongField, new LongConverter()),
  /** Float types. */
  Float(obj -> obj instanceof Float || obj instanceof FloatField, new FloatConverter()),
  /** Double types. */
  Double(obj -> obj instanceof Double || obj instanceof DoubleField, new DoubleConverter()),
  /** Class types. */
  Serializable(
      obj ->
          obj.getClass()
                  .isAnnotationPresent(
                      conversion.bittransfer.serialisation.types.Serializable.class)
              || obj instanceof SerializableField,
      new SerializableConverter()),
  /** String types. */
  String(obj -> obj instanceof String || obj instanceof StringField, new StringConverter()),
  ;

  private final Matcher matcher;
  private final Converter<Object> converter;

  /**
   * Internal constructor.
   *
   * @param matcher the matcher for checking type compatibility
   * @param converter the corresponding converter
   */
  @SuppressWarnings("unchecked")
  Types(Matcher matcher, Converter<?> converter) {
    this.matcher = matcher;
    this.converter = (Converter<Object>) converter;
  }

  /**
   * Get the corresponding type enum for the specified object.
   *
   * @param obj the object
   * @return the corresponding type
   */
  public static Types from(Object obj) {
    for (Types type : Types.values()) {
      if (type.matcher.match(obj)) {
        return type;
      }
    }
    throw new IllegalArgumentException("Unsupported type: " + obj.getClass().getSimpleName());
  }

  /**
   * Getter for the converter of this type.
   *
   * @return the converter
   */
  public Converter<Object> getConverter() {
    return converter;
  }

  interface Matcher {
    boolean match(Object obj);
  }
}
