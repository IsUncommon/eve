package uncmn.eve.store.sql;

import java.util.concurrent.TimeUnit;
import uncmn.eve.PostOperations;
import uncmn.eve.Value;

/**
 * An object instance representing SqlPostOperations.
 */

class SqlPostOperations extends PostOperations {

  SqlPostOperations(Value value, Action1 action) {
    super(value, action);
  }

  public static SqlPostOperations create(Value value, Action1 action) {
    return new SqlPostOperations(value, action);
  }

  @Override public PostOperations age(int maxAge, TimeUnit timeUnit) {
    value = value.toBuilder().age(maxAge, timeUnit).build();
    return this;
  }
}
