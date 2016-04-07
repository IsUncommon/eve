package is.uncommon.eve;

/**
 * A converter to serialize object into byte[] and deserialize object from byte[]
 */
public interface Converter {

    <T> T deserialize(byte[] data, Class<T> type);

    byte[] serialize(Object object, Class type);
}
