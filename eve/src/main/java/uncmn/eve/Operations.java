package uncmn.eve;

/**
 * Set of operations for known types of Java objects
 */
public interface Operations {

  void set(String key, int value);

  void set(String key, float value);

  void set(String key, double value);

  void set(String key, boolean value);

  void set(String key, char value);

  void set(String key, byte value);

  void set(String key, String value);

  void set(String key, Object value, Class type);
}
