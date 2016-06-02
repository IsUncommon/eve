package uncmn.eve;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Default converter that converts all the primitive types.
 * int, float, double, boolean, char, byte & String are supported.
 */
public class EveConverter implements Converter {
  private static final Map<String, Class<?>> forward = new HashMap<>();
  private static final Map<String, Serializer> serializers = new HashMap<>();
  private static final Map<Class<?>, String> backward = new HashMap<>();
  public static final Charset UTF_8 = Charset.forName("UTF-8");

  static {
    forward.put(Integer.class.getSimpleName(), Integer.class);
    forward.put(Float.class.getSimpleName(), Float.class);
    forward.put(Double.class.getSimpleName(), Double.class);
    forward.put(Long.class.getSimpleName(), Long.class);
    forward.put(Boolean.class.getSimpleName(), Boolean.class);
    forward.put(Character.class.getSimpleName(), Character.class);
    forward.put(Byte.class.getSimpleName(), Byte.class);
    forward.put(String.class.getSimpleName(), String.class);

    for (Map.Entry<String, Class<?>> entry : forward.entrySet()) {
      backward.put(entry.getValue(), entry.getKey());
    }

    serializers.put(Integer.class.getSimpleName(), new IntSerializer());
    serializers.put(Float.class.getSimpleName(), new FloatSerializer());
    serializers.put(Double.class.getSimpleName(), new DoubleSerializer());
    serializers.put(Long.class.getSimpleName(), new LongSerializer());
    serializers.put(Boolean.class.getSimpleName(), new BooleanSerializer());
    serializers.put(Character.class.getSimpleName(), new CharSerializer());
    serializers.put(Byte.class.getSimpleName(), new ByteSerializer());
    serializers.put(String.class.getSimpleName(), new StringSerializer());
  }

  EveConverter() {
  }

  /**
   * @param data data bytes that are serialized to disk.
   * @param converterKey Converter key.
   * @param <T> type T.
   * @return converted bytes.
   */
  @Override @SuppressWarnings("unchecked") public <T> T deserialize(byte[] data,
      String converterKey) {
    return (T) serializers.get(converterKey).deserialize(data);
  }

  /**
   * @param object Object to be serialized into byte[].
   * @return byte array of the object.
   */
  @Override public byte[] serialize(Object object) {
    return serializers.get(mapping(object)).serialize(object);
  }

  /**
   * @param object object for which mapping should be supported.
   * @return converter key if one exists, null otherwise.
   */
  @Override public String mapping(Object object) {
    return backward.get(object.getClass());
  }

  /**
   * @param converterKey converter key for which eve converter is supported.
   * @return true if eve converter supports this object conversion, false otherwise.
   */
  public boolean hasMapping(String converterKey) {
    return forward.containsKey(converterKey);
  }

  /**
   * Eve serializer.
   */
  interface Serializer {
    /**
     * @param value Value to be serialized.
     * @return byte[] representation of value.
     */
    byte[] serialize(Object value);

    /**
     * @param bytes bytes to be converted.
     * @return Object representation of bytes.
     */
    Object deserialize(byte[] bytes);
  }

  /**
   * Int serializer.
   */
  static class IntSerializer implements Serializer {
    public IntSerializer() {
    }

    @Override public byte[] serialize(Object value) {
      return ByteBuffer.allocate(4).putInt((int) value).array();
    }

    @Override public Object deserialize(byte[] bytes) {
      ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
      return byteBuffer.getInt();
    }
  }

  /**
   * Float serializer.
   */
  static class FloatSerializer implements Serializer {
    public FloatSerializer() {
    }

    @Override public byte[] serialize(Object value) {
      return ByteBuffer.allocate(4).putFloat((float) value).array();
    }

    @Override public Object deserialize(byte[] bytes) {
      ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
      return byteBuffer.getFloat();
    }
  }

  /**
   * Double serializer.
   */
  static class DoubleSerializer implements Serializer {
    public DoubleSerializer() {
    }

    @Override public byte[] serialize(Object value) {
      return ByteBuffer.allocate(8).putDouble((double) value).array();
    }

    @Override public Object deserialize(byte[] bytes) {
      ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
      return byteBuffer.getDouble();
    }
  }

  /**
   * Long serializer.
   */
  static class LongSerializer implements Serializer {
    public LongSerializer() {
    }

    @Override public byte[] serialize(Object value) {
      return ByteBuffer.allocate(8).putLong((long) value).array();
    }

    @Override public Object deserialize(byte[] bytes) {
      ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
      return byteBuffer.getLong();
    }
  }

  /**
   * Boolean serializer.
   */
  static class BooleanSerializer implements Serializer {
    public BooleanSerializer() {
    }

    @Override public byte[] serialize(Object value) {
      return ByteBuffer.allocate(1).put(((boolean) value) ? (byte) 1 : (byte) 0).array();
    }

    @Override public Object deserialize(byte[] bytes) {
      ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
      byte val = byteBuffer.get();
      return val == ((byte) 1);
    }
  }

  /**
   * Char serializer.
   */
  static class CharSerializer implements Serializer {
    public CharSerializer() {
    }

    @Override public byte[] serialize(Object value) {
      return ByteBuffer.allocate(2).putChar((char) value).array();
    }

    @Override public Object deserialize(byte[] bytes) {
      ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
      return byteBuffer.getChar();
    }
  }

  /**
   * Byte serializer.
   */
  static class ByteSerializer implements Serializer {
    public ByteSerializer() {
    }

    @Override public byte[] serialize(Object value) {
      return ByteBuffer.allocate(1).put((byte) value).array();
    }

    @Override public Object deserialize(byte[] bytes) {
      ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
      return byteBuffer.get();
    }
  }

  /**
   * String serializer.
   */
  static class StringSerializer implements Serializer {
    public StringSerializer() {
    }

    @Override public byte[] serialize(Object value) {
      return ((String) value).getBytes(UTF_8);
    }

    @Override public Object deserialize(byte[] bytes) {
      return new String(bytes, UTF_8);
    }
  }
}
