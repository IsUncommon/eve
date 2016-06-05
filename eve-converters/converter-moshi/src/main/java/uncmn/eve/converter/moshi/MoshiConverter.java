package uncmn.eve.converter.moshi;

import com.squareup.moshi.Moshi;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import okio.Buffer;
import uncmn.eve.Converter;

/**
 * An object instance representing MoshiConverter.
 * <p> Serialize an object to bytes and vice versa.</p>
 */
public class MoshiConverter implements Converter {

  Moshi moshi;
  private HashMap<String, Class<?>> converterMappings = new HashMap<>();

  MoshiConverter(Moshi moshi) {
    this.moshi = moshi;
  }

  /**
   * @param converterKey Unique converter key. This is the key that will be persisted in the db.
   * ConverterKey cannot start with prefix "eve.", it is used internally.
   * @param converterClass Class that should be mapped for this key.
   */
  public <T> void map(String converterKey, Class<T> converterClass) {
    converterMappings.put(converterKey, converterClass);
  }

  /**
   * @param moshi - {@link Moshi} instance.
   * @return {@link MoshiConverter}
   */
  public static MoshiConverter create(Moshi moshi) {
    return new MoshiConverter(moshi);
  }

  private <T> T deserialize(byte[] data, Class<T> type) {
    if (moshi == null || data == null || type == null) {
      return null;
    }

    Buffer buffer = new Buffer();
    buffer.write(data);

    try {
      return (T) moshi.adapter(type).fromJson(buffer);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * @param data data bytes that are serialized to disk.
   * @param converterKey Converter key.
   * @param <T> type T.
   * @return Object of type T.
   */
  @Override @SuppressWarnings("unchecked") public <T> T deserialize(byte[] data,
      String converterKey) {
    return deserialize(data, (Class<T>) converterMappings.get(converterKey));
  }

  @SuppressWarnings("unchecked") private byte[] serialize(Object object, Class type) {
    if (type == null) {
      throw new IllegalArgumentException("Class type cannot be null. Maybe map() first.");
    }
    Buffer buffer = new Buffer();
    try {
      moshi.adapter(type).toJson(buffer, object);
      return buffer.readByteArray();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return new byte[0];
  }

  /**
   * @param object Object to be serialized into byte[].
   * @return byte[] representation of object.
   */
  @Override public byte[] serialize(Object object) {
    return serialize(object, converterMappings.get(mapping(object)));
  }

  /**
   * @param object Object for which converter needs to be checked.
   * @return converter key if this object can be converted.
   */
  @Override public String mapping(Object object) {
    for (Map.Entry<String, Class<?>> item : converterMappings.entrySet()) {
      boolean bool = item.getValue().isAssignableFrom(object.getClass());
      if (bool) {
        return item.getKey();
      }
    }
    return null;
  }

  @Override public String mapping(Class<?> clazz) {
    for (Map.Entry<String, Class<?>> item : converterMappings.entrySet()) {
      boolean bool = item.getValue().isAssignableFrom(clazz);
      if (bool) {
        return item.getKey();
      }
    }
    return null;
  }

  @Override public Class mapType(String converterKey) {
    return converterMappings.get(converterKey);
  }
}
