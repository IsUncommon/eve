package uncmn.eve;

import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public abstract class Store implements Operations {

  private Converter converter;

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
    set(key, value(ByteBuffer.allocate(4).putInt(value).array(), int.class));
  }

  /**
   * Store {@link float} or {@link Float}.
   *
   * @param key is a {@link String}, NotNull and Unique
   * @param value float
   */
  @Override public void set(String key, float value) {
    set(key, value(ByteBuffer.allocate(4).putFloat(value).array(), float.class));
  }

  /**
   * Store {@link double} or {@link Double}.
   *
   * @param key is a {@link String}, NotNull and Unique
   * @param value double
   */
  @Override public void set(String key, double value) {
    set(key, value(ByteBuffer.allocate(4).putDouble(value).array(), double.class));
  }

  /**
   * Store {@link boolean} or {@link Boolean}.
   *
   * @param key is a {@link String}, NotNull and Unique
   * @param value boolean
   */
  @Override public void set(String key, boolean value) {
    int v = value ? 1 : 0;
    set(key, value(ByteBuffer.allocate(4).putInt(v).array(), boolean.class));
  }

  /**
   * Store {@link char} or {@link Character}.
   *
   * @param key is a {@link String}, NotNull and Unique
   * @param value char
   */
  @Override public void set(String key, char value) {
    set(key, value(ByteBuffer.allocate(4).putChar(value).array(), char.class));
  }

  /**
   * Store {@link byte} or {@link Byte}.
   *
   * @param key is a {@link String}, NotNull and Unique
   * @param value byte
   */
  @Override public void set(String key, byte value) {
    set(key, value(ByteBuffer.allocate(4).put(value).array(), byte.class));
  }

  /**
   * Store {@link String}.
   *
   * @param key is a {@link String}, NotNull and Unique
   * @param value {@link String}
   */
  @Override public void set(String key, String value) {
    set(key, value(value.getBytes(Charset.forName("UTF-8")), String.class));
  }

  /**
   * Store an Object of any kind into.
   *
   * @param key is a {@link String}, NotNull and Unique
   * @param object Object
   */
  @Override public void set(String key, Object object) {
    String converterKey = converter.converterKey(object);
    if (converterKey == null) {
      throw new RuntimeException("Object cannot be converted");
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
   * Build {@link Value} for {@link Class} .
   *
   * @param value byte array of the Object
   * @param type {@link Class}
   * @return {@link Value}
   */
  protected Value value(byte[] value, Class type) {
    return Value.builder().value(value).type(type).build();
  }

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
    return (T) converter.deserialize(value, converterKey);
  }

  /**
   * Convert the byte array into {@link Class} type.
   *
   * @param value byte array
   * @param type {@link Class}
   * @param <T> generic return type
   * @return generic Class type
   */
  @SuppressWarnings({ "unchecked", "UnusedDeclaration" }) protected <T> T convert(byte[] value,
      Type type) {

    if (type == null) {
      return null;
    }

    ByteBuffer byteBuffer = ByteBuffer.wrap(value);
    Object object = null;
    if (int.class.equals(type)) {
      object = byteBuffer.getInt();
    } else if (float.class.equals(type)) {
      object = byteBuffer.getFloat();
    } else if (double.class.equals(type)) {
      object = byteBuffer.getDouble();
    } else if (char.class.equals(type)) {
      char c = 0;
      if (value.length > 0) {
        c = byteBuffer.getChar();
      }
      object = c;
    } else if (boolean.class.equals(type)) {
      object = byteBuffer.getInt() == 1;
    } else if (short.class.equals(type)) {
      object = byteBuffer.getShort();
    } else if (long.class.equals(type)) {
      object = byteBuffer.getLong();
    } else if (byte.class.equals(type)) {
      object = byteBuffer.get();
    } else if (String.class.equals(type)) {
      object = new String(value, Charset.defaultCharset());
    }

    if (object == null) {
      return null;
    } else {
      return (T) object;
    }
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
