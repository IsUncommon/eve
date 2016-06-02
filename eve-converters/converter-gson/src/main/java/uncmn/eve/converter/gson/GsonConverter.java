package uncmn.eve.converter.gson;

import com.google.gson.Gson;
import uncmn.eve.Converter;

/**
 * An object instance representing GsonConverter.
 */

public class GsonConverter implements Converter {

  Gson gson;

  GsonConverter(Gson gson) {
    this.gson = gson;
  }

  public static GsonConverter create(Gson gson) {
    return new GsonConverter(gson);
  }

  @Override public <T> T deserialize(byte[] data, Class<T> type) {
    //TODO implement gson deserialization
    return null;
  }

  @Override public byte[] serialize(Object object, Class type) {
    //TODO implement gson serialization
    return new byte[0];
  }
}
