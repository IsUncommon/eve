package uncmn.eve;

import java.util.Collections;
import java.util.List;

/**
 * Query encapsulation on eve store for getting {@linkplain Entry}s.
 */
public class Query {

  static final int KEY_PREFIX = 1;
  static final int KEY_CONTAINS = 2;
  static final int CLASS_TYPE = 3;

  final Store store;
  String littleKey = null;
  int littleKeyType = -1;

  Query(Store store) {
    this.store = store;
  }

  /**
   * Find list of values with a key prefix.
   */
  public OfType keyPrefix(String prefix) {
    this.littleKeyType = KEY_PREFIX;
    this.littleKey = prefix;
    return OfType.create(this);
  }

  /**
   * Find list of values whose keys contain.
   */
  public OfType keyContains(String contains) {
    this.littleKeyType = KEY_CONTAINS;
    this.littleKey = contains;
    return OfType.create(this);
  }

  /**
   * Query objects of type.
   */
  public <T> QueryRunner<T> type(Class<T> cls) {
    return TypeQueryRunner.create(this, cls);
  }

  private boolean valid() {
    return littleKeyType > -1 || littleKey != null;
  }

  private boolean forPrefix() {
    return littleKeyType == KEY_PREFIX;
  }

  private boolean forContains() {
    return littleKeyType == KEY_CONTAINS;
  }

  public static class OfType {

    Query query;

    private OfType(Query query) {
      this.query = query;
    }

    static OfType create(Query query) {
      return new OfType(query);
    }

    /**
     * @param cls class instance.
     * @param <T> Type parameter. Type cannot be a collection type like List/Map etc or Generic.
     * type.
     */
    public <T> QueryRunner<T> type(final Class<T> cls) {
      if (query.forContains()) {
        return KeyContainsQueryRunner.create(query, cls);
      } else if (query.forPrefix()) {
        return KeyPrefixQueryRunner.create(query, cls);
      }
      return anyType();
    }

    /**
     * Fetch list of any type values/keys/entries.
     */
    public <R> QueryRunner<R> anyType() {
      return AnyTypeQueryRunner.create(query, null);
    }
  }

  public abstract static class QueryRunner<T> {

    Query query;
    Class<T> cls;

    QueryRunner(Query query, Class<T> cls) {
      this.query = query;
      this.cls = cls;
    }

    protected abstract boolean validate();

    /**
     * Execute query in store and get a list of entries.
     */
    public abstract List<Entry<T>> entries();

    /**
     * Get list of keys.
     */
    public abstract List<String> keys();

    /**
     * Get list of values.
     */
    public abstract List<T> values();
  }

  private static class TypeQueryRunner<T> extends QueryRunner<T> {

    TypeQueryRunner(Query query, Class<T> cls) {
      super(query, cls);
    }

    public static <T> TypeQueryRunner<T> create(Query query, Class<T> cls) {
      return new TypeQueryRunner<>(query, cls);
    }

    @Override protected boolean validate() {
      return cls != null;
    }

    @Override public List<Entry<T>> entries() {
      return query.store.entries(cls);
    }

    @Override public List<String> keys() {
      return query.store.keysType(cls);
    }

    @Override public List<T> values() {
      return query.store.valuesType(cls);
    }
  }

  private static class AnyTypeQueryRunner<T> extends QueryRunner<T> {

    AnyTypeQueryRunner(Query query, Class<T> cls) {
      super(query, cls);
    }

    static <R> AnyTypeQueryRunner<R> create(Query query, Class<R> cls) {
      return new AnyTypeQueryRunner<>(query, null);
    }

    @Override protected boolean validate() {
      return query.valid();
    }

    @Override public List<Entry<T>> entries() {
      System.out.println("Need type for entries.");
      return Collections.emptyList();
    }

    @Override public List<String> keys() {
      if (query.forPrefix()) {
        return query.store.keysPrefixAny(query.littleKey);
      } else {
        return query.store.keysContainsAny(query.littleKey);
      }
    }

    @SuppressWarnings("unchecked") @Override public List<T> values() {
      if (query.forPrefix()) {
        return (List<T>) query.store.valuesPrefixAny(query.littleKey);
      } else {
        return (List<T>) query.store.valuesContainsAny(query.littleKey);
      }
    }
  }

  private static class KeyPrefixQueryRunner<T> extends QueryRunner<T> {

    private KeyPrefixQueryRunner(Query query, Class<T> cls) {
      super(query, cls);
    }

    static <T> KeyPrefixQueryRunner<T> create(Query query, Class<T> cls) {
      return new KeyPrefixQueryRunner<>(query, cls);
    }

    @Override protected boolean validate() {
      if (!query.valid()) {
        throw new RuntimeException("Key prefix cannot be empty");
      }
      return true;
    }

    @Override public List<Entry<T>> entries() {
      return query.store.entriesKeyPrefix(cls, query.littleKey);
    }

    @Override public List<String> keys() {
      return query.store.keysPrefix(cls, query.littleKey);
    }

    @Override public List<T> values() {
      return query.store.valuesPrefix(cls, query.littleKey);
    }
  }

  private static class KeyContainsQueryRunner<T> extends QueryRunner<T> {

    private KeyContainsQueryRunner(Query query, Class<T> cls) {
      super(query, cls);
    }

    static <T> KeyContainsQueryRunner<T> create(Query query, Class<T> cls) {
      return new KeyContainsQueryRunner<>(query, cls);
    }

    @Override protected boolean validate() {
      if (!query.valid()) {
        throw new RuntimeException("Key Contains cannot be empty");
      }
      return true;
    }

    @Override public List<Entry<T>> entries() {
      return query.store.entriesKeyContains(cls, query.littleKey);
    }

    @Override public List<String> keys() {
      return query.store.keysContains(cls, query.littleKey);
    }

    @Override public List<T> values() {
      return query.store.valuesContains(cls, query.littleKey);
    }
  }
}
