package uncmn.eve;

import java.util.List;

/**
 * Query encapsulation on eve store for getting {@linkplain Entry}s.
 */
public class Query {

  final Store store;

  public Query(Store store) {
    this.store = store;
  }

  @SuppressWarnings("unchecked") public Query keyPrefix(String prefix) {
    return KeyPrefixQueryRunner.create(store, prefix);
  }

  @SuppressWarnings("unchecked") public Query keyContains(String contains) {
    return KeyContainsQueryRunner.create(store, contains);
  }

  protected void validate() {
    //
  }

  /**
   * @param clazz clazz instance.
   * @param <T> Type parameter. Type cannot be a collection type like List/Map etc or Generic type.
   */
  public <T> List<Entry<T>> run(Class<T> clazz) {
    return store.query(clazz);
  }

  static class KeyPrefixQueryRunner extends Query {
    String keyPrefix;

    public KeyPrefixQueryRunner(Store store) {
      super(store);
    }

    static KeyPrefixQueryRunner create(Store store, String keyPrefix) {
      KeyPrefixQueryRunner queryRunner = new KeyPrefixQueryRunner(store);
      queryRunner.keyPrefix = keyPrefix;
      return queryRunner;
    }

    @Override protected void validate() {
      if (keyPrefix == null || keyPrefix.length() == 0) {
        throw new RuntimeException("Key prefix cannot be empty");
      }
    }

    @Override @SuppressWarnings("unchecked") public <T> List<Entry<T>> run(Class<T> clazz) {
      validate();
      return store.queryKeyPrefix(clazz, keyPrefix);
    }
  }

  static class KeyContainsQueryRunner extends Query {
    String keyContains;

    KeyContainsQueryRunner(Store store) {
      super(store);
    }

    static KeyContainsQueryRunner create(Store store, String keyContains) {
      KeyContainsQueryRunner queryRunner = new KeyContainsQueryRunner(store);
      queryRunner.keyContains = keyContains;
      return queryRunner;
    }

    @Override protected void validate() {
      super.validate();
      if (keyContains == null || keyContains.length() == 0) {
        throw new RuntimeException("Key contains cannot be empty");
      }
    }

    @Override @SuppressWarnings("unchecked") public <T> List<Entry<T>> run(Class<T> clazz) {
      validate();
      return store.queryKeyContains(clazz, keyContains);
    }
  }
}
