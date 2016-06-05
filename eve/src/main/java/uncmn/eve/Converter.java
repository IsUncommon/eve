package uncmn.eve;

/**
 * A converter to serialize object into byte[] and deserialize object from byte[].
 */
public interface Converter {

  /**
   * @param data data bytes that are serialized to disk.
   * @param converterKey Converter key.
   * @param <T> Type.
   * @return Deserialized object of type <T>.
   */
  <T> T deserialize(byte[] data, String converterKey);

  /**
   * @param object Object to be serialized into byte[].
   */
  byte[] serialize(Object object);

  /**
   * @return Converter key of a given object. Null if cannot be determined.
   */
  String mapping(Object object);

  /**
   * @return Converter key of a given class. Null if cannot be determined.
   */
  String mapping(Class<?> clazz);

  /**
   * @return return the type for a given converter key.
   */
  Class mapType(String converterKey);
}
