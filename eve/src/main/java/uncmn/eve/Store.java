package uncmn.eve;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class Store implements Operations {
  private static final String LIST_KEY_PREFIX = "eve.list-";
  private static final Object storeLock = new Object();

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
    synchronized (storeLock) {
      set(key, value(eveConverter.serialize(value), type));
    }
  }

  /**
   * Store {@link int} array.
   *
   * @param key is a {@link String}, NotNull and Unique
   * @param value int
   */
  @Override public void set(String key, int[] value) {
    String type = eveConverter.mapping(value);
    synchronized (storeLock) {
      set(key, value(eveConverter.serialize(value), type));
    }
  }

  /**
   * Store {@link float} or {@link Float}.
   *
   * @param key is a {@link String}, NotNull and Unique
   * @param value float
   */
  @Override public void set(String key, float value) {
    String type = eveConverter.mapping(value);
    synchronized (storeLock) {
      set(key, value(eveConverter.serialize(value), type));
    }
  }

  /**
   * Store {@link float} array.
   *
   * @param key is a {@link String}, NotNull and Unique
   * @param value float array
   */
  @Override public void set(String key, float[] value) {
    String type = eveConverter.mapping(value);
    synchronized (storeLock) {
      set(key, value(eveConverter.serialize(value), type));
    }
  }

  /**
   * Store {@link long} or {@link Long}.
   *
   * @param key is a {@link String}, NotNull and Unique
   * @param value long
   */
  @Override public void set(String key, long value) {
    String type = eveConverter.mapping(value);
    synchronized (storeLock) {
      set(key, value(eveConverter.serialize(value), type));
    }
  }

  /**
   * Store {@link long} array.
   *
   * @param key is a {@link String}, NotNull and Unique
   * @param value long
   */
  @Override public void set(String key, long[] value) {
    String type = eveConverter.mapping(value);
    synchronized (storeLock) {
      set(key, value(eveConverter.serialize(value), type));
    }
  }

  /**
   * Store {@link double} or {@link Double}.
   *
   * @param key is a {@link String}, NotNull and Unique
   * @param value double
   */
  @Override public void set(String key, double value) {
    String type = eveConverter.mapping(value);
    synchronized (storeLock) {
      set(key, value(eveConverter.serialize(value), type));
    }
  }

  /**
   * Store {@link double} array.
   *
   * @param key is a {@link String}, NotNull and Unique
   * @param value double array
   */
  @Override public void set(String key, double[] value) {
    String type = eveConverter.mapping(value);
    synchronized (storeLock) {
      set(key, value(eveConverter.serialize(value), type));
    }
  }

  /**
   * Store {@link boolean} or {@link Boolean}.
   *
   * @param key is a {@link String}, NotNull and Unique
   * @param value boolean
   */
  @Override public void set(String key, boolean value) {
    String type = eveConverter.mapping(value);
    synchronized (storeLock) {
      set(key, value(eveConverter.serialize(value), type));
    }
  }

  /**
   * Store {@link char} or {@link Character}.
   *
   * @param key is a {@link String}, NotNull and Unique
   * @param value char
   */
  @Override public void set(String key, char value) {
    String type = eveConverter.mapping(value);
    synchronized (storeLock) {
      set(key, value(eveConverter.serialize(value), type));
    }
  }

  /**
   * Store {@link char} array.
   *
   * @param key is a {@link String}, NotNull and Unique
   * @param value char array
   */
  @Override public void set(String key, char[] value) {
    String type = eveConverter.mapping(value);
    synchronized (storeLock) {
      set(key, value(eveConverter.serialize(value), type));
    }
  }

  /**
   * Store {@link byte} or {@link Byte}.
   *
   * @param key is a {@link String}, NotNull and Unique
   * @param value byte
   */
  @Override public void set(String key, byte value) {
    String type = eveConverter.mapping(value);
    synchronized (storeLock) {
      set(key, value(eveConverter.serialize(value), type));
    }
  }

  /**
   * Store {@link byte} array
   *
   * @param key is a {@link String}, NotNull and Unique
   * @param value byte array.
   */
  @Override public void set(String key, byte[] value) {
    String type = eveConverter.mapping(value);
    synchronized (storeLock) {
      set(key, value(value, type));
    }
  }

  /**
   * Store {@link String}.
   *
   * @param key is a {@link String}, NotNull and Unique
   * @param value {@link String}
   */
  @Override public void set(String key, String value) {
    String type = eveConverter.mapping(value);
    synchronized (storeLock) {
      set(key, value(eveConverter.serialize(value), type));
    }
  }

  /**
   * Store {@link String} array.
   *
   * @param key is a {@link String}, NotNull and Unique
   * @param value {@link String} array
   */
  @Override public void set(String key, String[] value) {
    String type = eveConverter.mapping(value);
    synchronized (storeLock) {
      set(key, value(eveConverter.serialize(value), type));
    }
  }

  /**
   * Store an Object of any kind into.
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
    synchronized (storeLock) {
      set(key, value(converter.serialize(object), converterKey));
    }
  }

  /**
   * @param key Unique key string.
   * @param value A non empty homogeneous list of objects. Entire list should contain objects of
   */
  @Override public void set(String key, List<?> value) {
    if (value.isEmpty()) {
      throw new RuntimeException("List to be stored cannot be empty");
    }
    synchronized (storeLock) {
      String type = eveConverter.mapping(value);
      if (type != null) {
        set(key, value(eveConverter.serialize(value), type));
      } else {
        Object object = value.get(0);
        String converterKey = converter.mapping(object);
        if (converterKey == null) {
          throw new RuntimeException("Have you mapped object with converter.mapping() ? "
              + "Object cannot be converted -- "
              + object);
        }

        byte[][] objectBytes = new byte[value.size()][];
        int totalSize = 4; //first size of the array.
        for (int i = 0; i < value.size(); i++) {
          objectBytes[i] = converter.serialize(value.get(i));
          totalSize = totalSize + 4 + objectBytes[i].length;
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(totalSize);
        byteBuffer.putInt(value.size());
        for (byte[] objectByte : objectBytes) {
          byteBuffer.putInt(objectByte.length);
          byteBuffer.put(objectByte);
        }
        String listKey = LIST_KEY_PREFIX + converterKey;
        set(key, value(byteBuffer.array(), listKey));
      }
    }
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
    synchronized (storeLock) {
      if (eveConverter.hasMapping(converterKey)) {
        return (T) eveConverter.deserialize(value, converterKey);
      } else if (converterKey.startsWith(LIST_KEY_PREFIX)) {
        return convertList(value, converterKey);
      }
      return (T) converter.deserialize(value, converterKey);
    }
  }

  /**
   * @param value byte[] to be converted.
   * @param converterKey Converter key.
   * @param <T> type to be converted to.
   * @return converted list.
   */
  @SuppressWarnings({ "unchecked", "UnusedDeclaration" }) protected <T> T convertList(byte[] value,
      String converterKey) {
    String actualConverterKey = converterKey.split(LIST_KEY_PREFIX)[1];
    ByteBuffer byteBuffer = ByteBuffer.wrap(value);
    int size = byteBuffer.getInt();
    Object[] objects = new Object[size];
    for (int i = 0; i < size; i++) {
      int nextSize = byteBuffer.getInt();
      byte[] nextBytes = new byte[nextSize];
      byteBuffer.get(nextBytes);
      objects[i] = converter.deserialize(nextBytes, actualConverterKey);
    }
    return (T) Arrays.asList(objects);
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

  <T> List<Entry<T>> entriesKeyPrefix(Class<T> clazz, String keyPrefix) {
    String converterKey = eveConverter.mapping(clazz);
    if (converterKey == null) {
      converterKey = converter.mapping(clazz);
    }
    return entriesKeyPrefix(converterKey, keyPrefix);
  }

  protected abstract <T> List<Entry<T>> entriesKeyPrefix(String converterKey, String keyPrefix);

  <T> List<Entry<T>> entriesKeyContains(Class<T> clazz, String keyContains) {
    String converterKey = eveConverter.mapping(clazz);
    if (converterKey == null) {
      converterKey = converter.mapping(clazz);
    }
    return entriesKeyContains(converterKey, keyContains);
  }

  protected abstract <T> List<Entry<T>> entriesKeyContains(String converterKey, String keyContains);

  @Override public Query query() {
    return new Query(this);
  }

  <T> List<Entry<T>> query(Class<T> clazz) {
    String converterKey = eveConverter.mapping(clazz);
    if (converterKey == null) {
      converterKey = converter.mapping(clazz);
    }
    return entries(converterKey);
  }

  protected abstract <T> List<Entry<T>> entries(String converterKey);

  protected abstract List<String> keysPrefixAny(String keyPrefix);

  protected abstract List<String> keysContainsAny(String keyContains);

  <T> List<String> keysPrefix(Class<T> clazz, String keyPrefix) {
    if (clazz == null) {
      return Collections.emptyList();
    }
    String converterKey = eveConverter.mapping(clazz);
    if (converterKey == null) {
      converterKey = converter.mapping(clazz);
    }
    return keysPrefix(converterKey, keyPrefix);
  }

  protected abstract List<String> keysPrefix(String converterKey, String keyPrefix);

  <T> List<String> keysContains(Class<T> clazz, String keyContains) {
    if (clazz == null) {
      return Collections.emptyList();
    }
    String converterKey = eveConverter.mapping(clazz);
    if (converterKey == null) {
      converterKey = converter.mapping(clazz);
    }
    return keysContains(converterKey, keyContains);
  }

  protected abstract List<String> keysContains(String converterKey, String keyContains);

  protected abstract List<Object> valuesPrefixAny(String keyPrefix);

  protected abstract List<Object> valuesContainsAny(String keyContains);

  <T> List<T> valuesPrefix(Class<T> clazz, String keyPrefix) {
    if (clazz == null) {
      return Collections.emptyList();
    }
    String converterKey = eveConverter.mapping(clazz);
    if (converterKey == null) {
      converterKey = converter.mapping(clazz);
    }
    return valuesPrefix(converterKey, keyPrefix);
  }

  protected abstract <T> List<T> valuesPrefix(String converterKey, String keyPrefix);

  <T> List<T> valuesContains(Class<T> clazz, String keyContains) {
    if (clazz == null) {
      return Collections.emptyList();
    }
    String converterKey = eveConverter.mapping(clazz);
    if (converterKey == null) {
      converterKey = converter.mapping(clazz);
    }
    return valuesContains(converterKey, keyContains);
  }

  protected abstract <T> List<T> valuesContains(String converterKey, String keyContains);
}
