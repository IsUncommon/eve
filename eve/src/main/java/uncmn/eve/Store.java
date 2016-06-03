package uncmn.eve;

import java.util.List;

public abstract class Store implements Operations {

  private Converter converter;
  private EveConverter eveConverter = new EveConverter();

  public Store(Converter converter) {
    this.converter = converter;
  }

  /**
   * Store {@link int} or {@link Integer} values.
   *
   * @param key is a {@link String}, NotNull and Unique
   * @param value int
   */
  @Override public void set(String key, int value) {
    String type = eveConverter.mapping(value);
    set(key, value(eveConverter.serialize(value), type));
  }

  /**
   * Store {@link int} array.
   *
   * @param key is a {@link String}, NotNull and Unique
   * @param value int
   */
  @Override public void set(String key, int[] value) {
    String type = eveConverter.mapping(value);
    set(key, value(eveConverter.serialize(value), type));
  }

  /**
   * Store {@link float} or {@link Float}.
   *
   * @param key is a {@link String}, NotNull and Unique
   * @param value float
   */
  @Override public void set(String key, float value) {
    String type = eveConverter.mapping(value);
    set(key, value(eveConverter.serialize(value), type));
  }

  /**
   * Store {@link float} array.
   *
   * @param key is a {@link String}, NotNull and Unique
   * @param value float array
   */
  @Override public void set(String key, float[] value) {
    String type = eveConverter.mapping(value);
    set(key, value(eveConverter.serialize(value), type));
  }

  /**
   * Store {@link long} or {@link Long}.
   *
   * @param key is a {@link String}, NotNull and Unique
   * @param value long
   */
  @Override public void set(String key, long value) {
    String type = eveConverter.mapping(value);
    set(key, value(eveConverter.serialize(value), type));
  }

  /**
   * Store {@link long} array.
   *
   * @param key is a {@link String}, NotNull and Unique
   * @param value long
   */
  @Override public void set(String key, long[] value) {
    String type = eveConverter.mapping(value);
    set(key, value(eveConverter.serialize(value), type));
  }

  /**
   * Store {@link double} or {@link Double}.
   *
   * @param key is a {@link String}, NotNull and Unique
   * @param value double
   */
  @Override public void set(String key, double value) {
    String type = eveConverter.mapping(value);
    set(key, value(eveConverter.serialize(value), type));
  }

  /**
   * Store {@link double} array.
   *
   * @param key is a {@link String}, NotNull and Unique
   * @param value double array
   */
  @Override public void set(String key, double[] value) {
    String type = eveConverter.mapping(value);
    set(key, value(eveConverter.serialize(value), type));
  }

  /**
   * Store {@link boolean} or {@link Boolean}.
   *
   * @param key is a {@link String}, NotNull and Unique
   * @param value boolean
   */
  @Override public void set(String key, boolean value) {
    String type = eveConverter.mapping(value);
    set(key, value(eveConverter.serialize(value), type));
  }

  /**
   * Store {@link char} or {@link Character}.
   *
   * @param key is a {@link String}, NotNull and Unique
   * @param value char
   */
  @Override public void set(String key, char value) {
    String type = eveConverter.mapping(value);
    set(key, value(eveConverter.serialize(value), type));
  }

  /**
   * Store {@link char} array.
   *
   * @param key is a {@link String}, NotNull and Unique
   * @param value char array
   */
  @Override public void set(String key, char[] value) {
    String type = eveConverter.mapping(value);
    set(key, value(eveConverter.serialize(value), type));
  }

  /**
   * Store {@link byte} or {@link Byte}.
   *
   * @param key is a {@link String}, NotNull and Unique
   * @param value byte
   */
  @Override public void set(String key, byte value) {
    String type = eveConverter.mapping(value);
    set(key, value(eveConverter.serialize(value), type));
  }

  /**
   * Store {@link String}.
   *
   * @param key is a {@link String}, NotNull and Unique
   * @param value {@link String}
   */
  @Override public void set(String key, String value) {
    String type = eveConverter.mapping(value);
    set(key, value(eveConverter.serialize(value), type));
  }

  /**
   * Store {@link String} array.
   *
   * @param key is a {@link String}, NotNull and Unique
   * @param value {@link String} array
   */
  @Override public void set(String key, String[] value) {
    String type = eveConverter.mapping(value);
    set(key, value(eveConverter.serialize(value), type));
  }

  /**
   * Store {@link String} array.
   *
   * @param key is a {@link String}, NotNull and Unique
   * @param value {@link String} list, Cannot be empty.
   */
  @Override public void set(String key, List<String> value) {
    if (value.isEmpty()) {
      throw new RuntimeException("List to be stored cannot be empty");
    }
    String type = eveConverter.mapping(value);
    set(key, value(eveConverter.serialize(value), type));
  }

  /**
   * Store an Object of any kind into. Do not use this method to store collection types.
   *
   * @param key is a {@link String}, NotNull and Unique
   * @param object Object
   */
  @Override public void set(String key, Object object) {
    String converterKey = converter.mapping(object);
    if (converterKey == null) {
      throw new RuntimeException("Have you mapped object with converter.mapping() ? "
          + "Object cannot be converted -- "
          + object);
    }
    set(key, value(converter.serialize(object), converterKey));
  }

  /**
   * Set key with value in custom store.
   *
   * @param key is a {@link String}, NotNull and Unique
   * @param value {@link Value}
   */
  public abstract void set(String key, Value value);

  /**
   * Build {@link Value}.
   *
   * @param value byte array of the Object
   * @param type {@link Class}
   * @return {@link Value}
   */
  protected Value value(byte[] value, String type) {
    return Value.builder().value(value).type(type).build();
  }

  /**
   * @param value byte[] to be converted.
   * @param converterKey Converter key.
   * @param <T> type to be converted to.
   * @return converted object.
   */
  @SuppressWarnings({ "unchecked", "UnusedDeclaration" }) protected <T> T convert(byte[] value,
      String converterKey) {
    if (eveConverter.hasMapping(converterKey)) {
      return (T) eveConverter.deserialize(value, converterKey);
    }
    return (T) converter.deserialize(value, converterKey);
  }

  /**
   * Print the value of the key.
   *
   * @param key is a {@link String}, NotNull and Unique
   */
  public void print(String key) {
    System.out.println(get(key) + "");
  }

  /**
   * Get value for key from store.
   *
   * @param key is a {@link String}, NotNull and Unique
   * @param <T> generic return type
   * @return generic return type
   */
  public abstract <T> T get(String key);

  /**
   * Delete key.
   *
   * @param key is a {@link String}, NotNull and Unique
   * @return true if deleted key false otherwise
   */
  public abstract boolean delete(String key);

  /**
   * Check if key exists in the Store.
   *
   * @param key is a {@link String}, NotNull and Unique
   * @return true if key exists false otherwise
   */
  public abstract boolean exists(String key);
}
