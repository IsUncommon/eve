package uncmn.eve;

import java.util.concurrent.TimeUnit;

/**
 * An object instance representing PostOperations.
 */

public abstract class PostOperations {

  public interface Action1 {
    void call(Value value);
  }

  protected Value value;
  private Action1 action;

  public PostOperations(Value value, Action1 action) {
    this.value = value;
    this.action = action;
  }

  public abstract PostOperations age(int maxAge, TimeUnit timeUnit);

  public void commit() {
    action.call(value);
  }
}
