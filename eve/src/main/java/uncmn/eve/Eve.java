package uncmn.eve;

/**
 * An object instance representing Eve.
 * <p>Main entry to the Eve KeyValue store</p>.
 */
public final class Eve {

  private Store store;

  Eve(Builder builder) {
    this.store = builder.store;
  }

  public static Builder builder() {
    return new Builder();
  }

  public Store store() {
    return store;
  }

  public static class Builder {

    Store store;

    Builder() {

    }

    public Builder store(Store store) {
      this.store = store;
      return this;
    }

    public Eve build() {
      return new Eve(this);
    }
  }
}
