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
  private static final Charset UTF_8 = Charset.forName("UTF-8");
  private static final int INT_BYTE_SIZE = Integer.SIZE / Byte.SIZE;
  private static final int FLOAT_BYTE_SIZE = Float.SIZE / Byte.SIZE;
  private static final int DOUBLE_BYTE_SIZE = Double.SIZE / Byte.SIZE;
  private static final int LONG_BYTE_SIZE = Long.SIZE / Byte.SIZE;
  private static final int BOOLEAN_BYTE_SIZE = 1;
  private static final int CHARACTER_BYTE_SIZE = Character.SIZE / Byte.SIZE;

  static {
    forward.put(Integer.class.getSimpleName(), Integer.class);
    forward.put(Float.class.getSimpleName(), Float.class);
    forward.put(Double.class.getSimpleName(), Double.class);
    forward.put(Long.class.getSimpleName(), Long.class);
    forward.put(Boolean.class.getSimpleName(), Boolean.class);
    forward.put(Character.class.getSimpleName(), Character.class);
    forward.put(Byte.class.getSimpleName(), Byte.class);
    forward.put(String.class.getSimpleName(), String.class);
    forward.put(int[].class.getSimpleName(), int[].class);
    forward.put(float[].class.getSimpleName(), float[].class);
    forward.put(double[].class.getSimpleName(), double[].class);
    forward.put(long[].class.getSimpleName(), long[].class);
    forward.put(char[].class.getSimpleName(), char[].class);
    forward.put(String[].class.getSimpleName(), String[].class);

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
    serializers.put(int[].class.getSimpleName(), new IntArraySerializer());
    serializers.put(float[].class.getSimpleName(), new FloatArraySerializer());
    serializers.put(double[].class.getSimpleName(), new DoubleArraySerializer());
    serializers.put(long[].class.getSimpleName(), new LongArraySerializer());
    serializers.put(char[].class.getSimpleName(), new CharArraySerializer());
    serializers.put(String[].class.getSimpleName(), new StringArraySerializer());
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
      return ByteBuffer.allocate(INT_BYTE_SIZE).putInt((int) value).array();
    }

    @Override public Object deserialize(byte[] bytes) {
      ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
      return byteBuffer.getInt();
    }
  }

  /**
   * Int Array serializer.
   */
  static class IntArraySerializer implements Serializer {
    public IntArraySerializer() {
    }

    @Override public byte[] serialize(Object value) {
      int[] ints = (int[]) value;
      ByteBuffer buffer = ByteBuffer.allocate(INT_BYTE_SIZE * ints.length);
      for (int i = 0; i < ints.length; i++) {
        buffer.putInt(ints[i]);
      }
      return buffer.array();
    }

    @Override public Object deserialize(byte[] bytes) {
      ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
      int[] ints = new int[bytes.length / INT_BYTE_SIZE];
      for (int i = 0; i < ints.length; i++) {
        ints[i] = byteBuffer.getInt();
      }
      return ints;
    }
  }

  /**
   * Float serializer.
   */
  static class FloatSerializer implements Serializer {
    public FloatSerializer() {
    }

    @Override public byte[] serialize(Object value) {
      return ByteBuffer.allocate(FLOAT_BYTE_SIZE).putFloat((float) value).array();
    }

    @Override public Object deserialize(byte[] bytes) {
      ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
      return byteBuffer.getFloat();
    }
  }

  /**
   * Float array serializer.
   */
  static class FloatArraySerializer implements Serializer {
    public FloatArraySerializer() {
    }

    @Override public byte[] serialize(Object value) {
      float[] floats = (float[]) value;
      ByteBuffer buffer = ByteBuffer.allocate(FLOAT_BYTE_SIZE * floats.length);
      for (int i = 0; i < floats.length; i++) {
        buffer.putFloat(floats[i]);
      }
      return buffer.array();
    }

    @Override public Object deserialize(byte[] bytes) {
      ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
      float[] floats = new float[bytes.length / FLOAT_BYTE_SIZE];
      for (int i = 0; i < floats.length; i++) {
        floats[i] = byteBuffer.getFloat();
      }
      return floats;
    }
  }

  /**
   * Double serializer.
   */
  static class DoubleSerializer implements Serializer {
    public DoubleSerializer() {
    }

    @Override public byte[] serialize(Object value) {
      return ByteBuffer.allocate(DOUBLE_BYTE_SIZE).putDouble((double) value).array();
    }

    @Override public Object deserialize(byte[] bytes) {
      ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
      return byteBuffer.getDouble();
    }
  }

  /**
   * Double array serializer.
   */
  static class DoubleArraySerializer implements Serializer {
    public DoubleArraySerializer() {
    }

    @Override public byte[] serialize(Object value) {
      double[] doubles = (double[]) value;
      ByteBuffer buffer = ByteBuffer.allocate(DOUBLE_BYTE_SIZE * doubles.length);
      for (int i = 0; i < doubles.length; i++) {
        buffer.putDouble(doubles[i]);
      }
      return buffer.array();
    }

    @Override public Object deserialize(byte[] bytes) {
      ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
      double[] doubles = new double[bytes.length / DOUBLE_BYTE_SIZE];
      for (int i = 0; i < doubles.length; i++) {
        doubles[i] = byteBuffer.getDouble();
      }
      return doubles;
    }
  }

  /**
   * Long serializer.
   */
  static class LongSerializer implements Serializer {
    public LongSerializer() {
    }

    @Override public byte[] serialize(Object value) {
      return ByteBuffer.allocate(LONG_BYTE_SIZE).putLong((long) value).array();
    }

    @Override public Object deserialize(byte[] bytes) {
      ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
      return byteBuffer.getLong();
    }
  }

  /**
   * Long array serializer.
   */
  static class LongArraySerializer implements Serializer {
    public LongArraySerializer() {
    }

    @Override public byte[] serialize(Object value) {
      long[] longs = (long[]) value;
      ByteBuffer buffer = ByteBuffer.allocate(LONG_BYTE_SIZE * longs.length);
      for (int i = 0; i < longs.length; i++) {
        buffer.putLong(longs[i]);
      }
      return buffer.array();
    }

    @Override public Object deserialize(byte[] bytes) {
      ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
      long[] longs = new long[bytes.length / LONG_BYTE_SIZE];
      for (int i = 0; i < longs.length; i++) {
        longs[i] = byteBuffer.getLong();
      }
      return longs;
    }
  }

  /**
   * Boolean serializer.
   */
  static class BooleanSerializer implements Serializer {
    public BooleanSerializer() {
    }

    @Override public byte[] serialize(Object value) {
      return ByteBuffer.allocate(BOOLEAN_BYTE_SIZE)
          .put(((boolean) value) ? (byte) 1 : (byte) 0)
          .array();
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
      return ByteBuffer.allocate(CHARACTER_BYTE_SIZE).putChar((char) value).array();
    }

    @Override public Object deserialize(byte[] bytes) {
      ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
      return byteBuffer.getChar();
    }
  }

  /**
   * Long array serializer.
   */
  static class CharArraySerializer implements Serializer {
    public CharArraySerializer() {
    }

    @Override public byte[] serialize(Object value) {
      char[] chars = (char[]) value;
      ByteBuffer buffer = ByteBuffer.allocate(CHARACTER_BYTE_SIZE * chars.length);
      for (int i = 0; i < chars.length; i++) {
        buffer.putChar(chars[i]);
      }
      return buffer.array();
    }

    @Override public Object deserialize(byte[] bytes) {
      ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
      char[] chars = new char[bytes.length / CHARACTER_BYTE_SIZE];
      for (int i = 0; i < chars.length; i++) {
        chars[i] = byteBuffer.getChar();
      }
      return chars;
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

  /**
   * String Array serializer.
   */
  static class StringArraySerializer implements Serializer {
    public StringArraySerializer() {
    }

    @Override public byte[] serialize(Object value) {
      String[] strings = (String[]) value;
      byte[][] stringsBytes = new byte[strings.length][];
      int totalSize = 4; //first size of the array.
      for (int i = 0; i < strings.length; i++) {
        stringsBytes[i] = strings[i].getBytes(UTF_8);
        totalSize = totalSize + 4 + stringsBytes[i].length;
      }
      ByteBuffer byteBuffer = ByteBuffer.allocate(totalSize);
      byteBuffer.putInt(strings.length);
      for (int i = 0; i < stringsBytes.length; i++) {
        byteBuffer.putInt(stringsBytes[i].length);
        byteBuffer.put(stringsBytes[i]);
      }
      return byteBuffer.array();
    }

    @Override public Object deserialize(byte[] bytes) {
      ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
      int size = byteBuffer.getInt();
      String[] strings = new String[size];
      for (int i = 0; i < size; i++) {
        int nextSize = byteBuffer.getInt();
        byte[] nextBytes = new byte[nextSize];
        byteBuffer.get(nextBytes);
        strings[i] = new String(nextBytes, UTF_8);
      }
      return strings;
    }
  }
}
