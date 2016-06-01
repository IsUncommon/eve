package uncmn.eve;

import android.content.Context;

/**
 * An object instance representing Eve.
 * <p>Main entry to the Eve KeyValue store</p>.
 */
public final class Eve {

  private Context context;
  private Store store;

  Eve(Builder builder) {
    this.context = builder.context;
    this.store = builder.store;
  }

  public static Builder builder(Context context) {
    return new Builder(context);
  }

  public Store store() {
    return store;
  }

  public static class Builder {

    Context context;
    Store store;

    Builder(Context context) {
      if (context == null) {
        throw new IllegalArgumentException("Context cannot be null");
      }
      this.context = context.getApplicationContext();
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
