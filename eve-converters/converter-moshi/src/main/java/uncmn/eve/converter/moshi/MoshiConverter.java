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

  public <T> T deserialize(byte[] data, Class<T> type) {
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

  public byte[] serialize(Object object, Class type) {
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
   *
   * @param data
   * @param converterKey
   * @param <T>
   * @return
   */
  @Override @SuppressWarnings("unchecked") public <T> T deserialize(byte[] data,
      String converterKey) {
    return deserialize(data, (Class<T>) converterMappings.get(converterKey));
  }

  @Override public byte[] serialize(Object object) {
    return serialize(object, converterMappings.get(mapping(object)));
  }

  @Override public String mapping(Object object) {
    for (Map.Entry<String, Class<?>> item : converterMappings.entrySet()) {
      boolean bool = item.getValue().isAssignableFrom(object.getClass());
      if (bool) {
        return item.getKey();
      }
    }
    return null;
  }
}
