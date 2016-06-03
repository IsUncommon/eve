package uncmn.eve;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Default converter that converts all the primitive types.
 * int, float, double, boolean, char, byte & String are supported.
 */
public class EveConverter implements Converter {
  private static final String EVE_PREFIX = "eve.";
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
  private static final String LIST_STRING_CONVERTER_KEY = EVE_PREFIX + "listString";

  static {
    forward.put(EVE_PREFIX + Integer.class.getSimpleName(), Integer.class);
    forward.put(EVE_PREFIX + Float.class.getSimpleName(), Float.class);
    forward.put(EVE_PREFIX + Double.class.getSimpleName(), Double.class);
    forward.put(EVE_PREFIX + Long.class.getSimpleName(), Long.class);
    forward.put(EVE_PREFIX + Boolean.class.getSimpleName(), Boolean.class);
    forward.put(EVE_PREFIX + Character.class.getSimpleName(), Character.class);
    forward.put(EVE_PREFIX + Byte.class.getSimpleName(), Byte.class);
    forward.put(EVE_PREFIX + String.class.getSimpleName(), String.class);
    forward.put(EVE_PREFIX + int[].class.getSimpleName(), int[].class);
    forward.put(EVE_PREFIX + float[].class.getSimpleName(), float[].class);
    forward.put(EVE_PREFIX + double[].class.getSimpleName(), double[].class);
    forward.put(EVE_PREFIX + long[].class.getSimpleName(), long[].class);
    forward.put(EVE_PREFIX + char[].class.getSimpleName(), char[].class);
    forward.put(EVE_PREFIX + String[].class.getSimpleName(), String[].class);

    for (Map.Entry<String, Class<?>> entry : forward.entrySet()) {
      backward.put(entry.getValue(), entry.getKey());
    }

    serializers.put(EVE_PREFIX + Integer.class.getSimpleName(), new IntSerializer());
    serializers.put(EVE_PREFIX + Float.class.getSimpleName(), new FloatSerializer());
    serializers.put(EVE_PREFIX + Double.class.getSimpleName(), new DoubleSerializer());
    serializers.put(EVE_PREFIX + Long.class.getSimpleName(), new LongSerializer());
    serializers.put(EVE_PREFIX + Boolean.class.getSimpleName(), new BooleanSerializer());
    serializers.put(EVE_PREFIX + Character.class.getSimpleName(), new CharSerializer());
    serializers.put(EVE_PREFIX + Byte.class.getSimpleName(), new ByteSerializer());
    serializers.put(EVE_PREFIX + String.class.getSimpleName(), new StringSerializer());
    serializers.put(EVE_PREFIX + int[].class.getSimpleName(), new IntArraySerializer());
    serializers.put(EVE_PREFIX + float[].class.getSimpleName(), new FloatArraySerializer());
    serializers.put(EVE_PREFIX + double[].class.getSimpleName(), new DoubleArraySerializer());
    serializers.put(EVE_PREFIX + long[].class.getSimpleName(), new LongArraySerializer());
    serializers.put(EVE_PREFIX + char[].class.getSimpleName(), new CharArraySerializer());
    serializers.put(EVE_PREFIX + String[].class.getSimpleName(), new StringArraySerializer());
    serializers.put(LIST_STRING_CONVERTER_KEY, new ListStringSerializer());
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
    if (List.class.isAssignableFrom(object.getClass())) {
      //generic type erasure check, hack with first element
      List<?> items = (List<?>) object;
      if (!items.isEmpty()) {
        if (items.get(0) instanceof String) {
          return LIST_STRING_CONVERTER_KEY;
        }
      }
    }
    return backward.get(object.getClass());
  }

  @Override public String mapping(Class<?> clazz) {
    return backward.get(clazz);
  }

  /**
   * @param converterKey converter key for which eve converter is supported.
   * @return true if eve converter supports this object conversion, false otherwise.
   */
  public boolean hasMapping(String converterKey) {
    return forward.containsKey(converterKey) || LIST_STRING_CONVERTER_KEY.equals(converterKey);
  }

  /**
   * Eve serializer.
   */
  interface Serializer<T> {
    /**
     * @param value Value to be serialized.
     * @return byte[] representation of value.
     */
    byte[] serialize(T value);

    /**
     * @param bytes bytes to be converted.
     * @return Object representation of bytes.
     */
    T deserialize(byte[] bytes);
  }

  /**
   * Int serializer.
   */
  static class IntSerializer implements Serializer<Integer> {
    public IntSerializer() {
    }

    @Override public byte[] serialize(Integer value) {
      return ByteBuffer.allocate(INT_BYTE_SIZE).putInt((int) value).array();
    }

    @Override public Integer deserialize(byte[] bytes) {
      ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
      return byteBuffer.getInt();
    }
  }

  /**
   * Int Array serializer.
   */
  static class IntArraySerializer implements Serializer<int[]> {

    public IntArraySerializer() {
    }

    @Override public byte[] serialize(int[] value) {
      ByteBuffer buffer = ByteBuffer.allocate(INT_BYTE_SIZE * value.length);
      for (int i = 0; i < value.length; i++) {
        buffer.putInt(value[i]);
      }
      return buffer.array();
    }

    @Override public int[] deserialize(byte[] bytes) {
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
  static class FloatSerializer implements Serializer<Float> {
    public FloatSerializer() {
    }

    @Override public byte[] serialize(Float value) {
      return ByteBuffer.allocate(FLOAT_BYTE_SIZE).putFloat((float) value).array();
    }

    @Override public Float deserialize(byte[] bytes) {
      ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
      return byteBuffer.getFloat();
    }
  }

  /**
   * Float array serializer.
   */
  static class FloatArraySerializer implements Serializer<float[]> {
    public FloatArraySerializer() {
    }

    @Override public byte[] serialize(float[] value) {
      ByteBuffer buffer = ByteBuffer.allocate(FLOAT_BYTE_SIZE * value.length);
      for (int i = 0; i < value.length; i++) {
        buffer.putFloat(value[i]);
      }
      return buffer.array();
    }

    @Override public float[] deserialize(byte[] bytes) {
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
  static class DoubleSerializer implements Serializer<Double> {
    public DoubleSerializer() {
    }

    @Override public byte[] serialize(Double value) {
      return ByteBuffer.allocate(DOUBLE_BYTE_SIZE).putDouble(value).array();
    }

    @Override public Double deserialize(byte[] bytes) {
      ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
      return byteBuffer.getDouble();
    }
  }

  /**
   * Double array serializer.
   */
  static class DoubleArraySerializer implements Serializer<double[]> {
    public DoubleArraySerializer() {
    }

    @Override public byte[] serialize(double[] value) {
      ByteBuffer buffer = ByteBuffer.allocate(DOUBLE_BYTE_SIZE * value.length);
      for (int i = 0; i < value.length; i++) {
        buffer.putDouble(value[i]);
      }
      return buffer.array();
    }

    @Override public double[] deserialize(byte[] bytes) {
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
  static class LongSerializer implements Serializer<Long> {
    public LongSerializer() {
    }

    @Override public byte[] serialize(Long value) {
      return ByteBuffer.allocate(LONG_BYTE_SIZE).putLong(value).array();
    }

    @Override public Long deserialize(byte[] bytes) {
      ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
      return byteBuffer.getLong();
    }
  }

  /**
   * Long array serializer.
   */
  static class LongArraySerializer implements Serializer<long[]> {
    public LongArraySerializer() {
    }

    @Override public byte[] serialize(long[] value) {
      ByteBuffer buffer = ByteBuffer.allocate(LONG_BYTE_SIZE * value.length);
      for (int i = 0; i < value.length; i++) {
        buffer.putLong(value[i]);
      }
      return buffer.array();
    }

    @Override public long[] deserialize(byte[] bytes) {
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
  static class BooleanSerializer implements Serializer<Boolean> {
    public BooleanSerializer() {
    }

    @Override public byte[] serialize(Boolean value) {
      return ByteBuffer.allocate(BOOLEAN_BYTE_SIZE).put((value) ? (byte) 1 : (byte) 0).array();
    }

    @Override public Boolean deserialize(byte[] bytes) {
      ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
      byte val = byteBuffer.get();
      return val == ((byte) 1);
    }
  }

  /**
   * Char serializer.
   */
  static class CharSerializer implements Serializer<Character> {
    public CharSerializer() {
    }

    @Override public byte[] serialize(Character value) {
      return ByteBuffer.allocate(CHARACTER_BYTE_SIZE).putChar(value).array();
    }

    @Override public Character deserialize(byte[] bytes) {
      ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
      return byteBuffer.getChar();
    }
  }

  /**
   * Long array serializer.
   */
  static class CharArraySerializer implements Serializer<char[]> {
    public CharArraySerializer() {
    }

    @Override public byte[] serialize(char[] value) {
      ByteBuffer buffer = ByteBuffer.allocate(CHARACTER_BYTE_SIZE * value.length);
      for (int i = 0; i < value.length; i++) {
        buffer.putChar(value[i]);
      }
      return buffer.array();
    }

    @Override public char[] deserialize(byte[] bytes) {
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
  static class ByteSerializer implements Serializer<Byte> {
    public ByteSerializer() {
    }

    @Override public byte[] serialize(Byte value) {
      return ByteBuffer.allocate(1).put((byte) value).array();
    }

    @Override public Byte deserialize(byte[] bytes) {
      ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
      return byteBuffer.get();
    }
  }

  /**
   * String serializer.
   */
  static class StringSerializer implements Serializer<String> {
    public StringSerializer() {
    }

    @Override public byte[] serialize(String value) {
      return (value).getBytes(UTF_8);
    }

    @Override public String deserialize(byte[] bytes) {
      return new String(bytes, UTF_8);
    }
  }

  /**
   * String Array serializer.
   */
  static class StringArraySerializer implements Serializer<String[]> {
    public StringArraySerializer() {
    }

    @Override public byte[] serialize(String[] value) {
      byte[][] stringsBytes = new byte[value.length][];
      int totalSize = 4; //first size of the array.
      for (int i = 0; i < value.length; i++) {
        stringsBytes[i] = value[i].getBytes(UTF_8);
        totalSize = totalSize + 4 + stringsBytes[i].length;
      }
      ByteBuffer byteBuffer = ByteBuffer.allocate(totalSize);
      byteBuffer.putInt(value.length);
      for (int i = 0; i < stringsBytes.length; i++) {
        byteBuffer.putInt(stringsBytes[i].length);
        byteBuffer.put(stringsBytes[i]);
      }
      return byteBuffer.array();
    }

    @Override public String[] deserialize(byte[] bytes) {
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

  /**
   * String List serializer.
   */
  static class ListStringSerializer implements Serializer<List<String>> {

    public ListStringSerializer() {
    }

    @Override public byte[] serialize(List<String> value) {

      String[] strings = new String[value.size()];
      value.toArray(strings);
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

    @Override public List<String> deserialize(byte[] bytes) {
      ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
      int size = byteBuffer.getInt();
      String[] strings = new String[size];
      for (int i = 0; i < size; i++) {
        int nextSize = byteBuffer.getInt();
        byte[] nextBytes = new byte[nextSize];
        byteBuffer.get(nextBytes);
        strings[i] = new String(nextBytes, UTF_8);
      }
      return Arrays.asList(strings);
    }
  }
}
