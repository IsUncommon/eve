package uncmn.eve;

/**
 * Encapsulates entry in eve. Key and a value of type T
 *
 * @param <T> value type T.
 */
public class Entry<T> {
  String key;
  T value;

  private Entry() {

  }

  /**
   * Creator.
   *
   * @param key Entry key.
   * @param value Entry value of type T.
   * @param <T> type parameter.
   * @return new instance.
   */
  public static final <T> Entry<T> create(String key, T value) {
    Entry<T> entry = new Entry<>();
    entry.key = key;
    entry.value = value;
    return entry;
  }

  @Override public String toString() {
    return "Entry{" + "key='" + key + '\'' + ", value=" + value + '}';
  }
}
