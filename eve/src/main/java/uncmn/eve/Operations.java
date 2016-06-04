package uncmn.eve;

import java.util.List;

/**
 * Set of operations for known types of Java objects.
 */
public interface Operations {

  /**
   * @param key Unique key string.
   * @param value int value.
   */
  void set(String key, int value);

  /**
   * @param key Unique key string.
   * @param value array of ints.
   */
  void set(String key, int[] value);

  /**
   * @param key Unique key string.
   * @param value float value.
   */
  void set(String key, float value);

  /**
   * @param key Unique key string.
   * @param value array of floats.
   */
  void set(String key, float[] value);

  /**
   * @param key Unique key string.
   * @param value double value.
   */
  void set(String key, double value);

  /**
   * @param key Unique key string.
   * @param value array of doubles.
   */
  void set(String key, double[] value);

  /**
   * @param key Unique key string.
   * @param value long value.
   */
  void set(String key, long value);

  /**
   * @param key Unique key string.
   * @param value array of longs.
   */
  void set(String key, long[] value);

  /**
   * @param key Unique key string.
   * @param value boolean value.
   */
  void set(String key, boolean value);

  /**
   * @param key Unique key string.
   * @param value char value.
   */
  void set(String key, char value);

  /**
   * @param key Unique key string.
   * @param value array of chars.
   */
  void set(String key, char[] value);

  /**
   * @param key Unique key string.
   * @param value byte value.
   */
  void set(String key, byte value);

  /**
   * @param key Unique key string.
   * @param value byte array.
   */
  void set(String key, byte[] value);

  /**
   * @param key Unique key string.
   * @param value String value.
   */
  void set(String key, String value);

  /**
   * @param key Unique key string.
   * @param value array of strings.
   */
  void set(String key, String[] value);

  /**
   * @param key Unique key string. Do not use this method to store collection types.
   * See {@linkplain #set(String, List)}
   * @param value An object that can be converted with {@linkplain Converter}.
   */
  void set(String key, Object value);

  /**
   * @param key Unique key string.
   * @param value A non empty homogeneous list of objects. Entire list should contain objects of
   * same type. This object should be convertable with {@linkplain Converter},
   * String list handled internally and does not need any converter mapping.
   */
  void set(String key, List<?> value);

  /**
   * Get value for key from store.
   *
   * @param key is a {@link String}, NotNull and Unique
   * @param <T> generic return type
   * @return generic return type
   */
  <T> T get(String key);

  /**
   * A generic query.
   *
   * @return {@linkplain Query} instance.
   */
  Query query();
}
