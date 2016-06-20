package uncmn.eve;

import java.util.concurrent.TimeUnit;

/**
 * An object instance representing Value.
 * <p> This wraps the value of a key with a type and custom identifiers.</p>
 */
public class Value {

  private byte[] bytes;
  private String type;
  private long expireAt;

  Value(Builder builder) {
    this.bytes = builder.value;
    this.type = builder.type;
    this.expireAt = builder.expireAt;
  }

  public static Builder builder() {
    return new Builder();
  }

  public Builder toBuilder() {
    return new Builder(this);
  }

  public void bytes(byte[] value) {
    this.bytes = value;
  }

  public byte[] bytes() {
    return bytes;
  }

  public void type(String type) {
    this.type = type;
  }

  public String type() {
    return type;
  }

  public long expireAt() {
    return expireAt;
  }

  public static class Builder {

    byte[] value;
    String type;
    long expireAt;

    Builder() {

    }

    Builder(Value value) {
      this.value = value.bytes;
      this.type = value.type;
      this.expireAt = value.expireAt;
    }

    public Builder value(byte[] value) {
      this.value = value;
      return this;
    }

    public Builder type(String type) {
      this.type = type;
      return this;
    }

    public Builder age(int maxAge, TimeUnit timeUnit) {
      this.expireAt = System.currentTimeMillis() + timeUnit.toMillis(maxAge);
      return this;
    }

    /**
     * Build the value object.
     *
     * @return Value object.
     */
    public Value build() {
      if (type == null) {
        throw new IllegalArgumentException("Type must be present");
      }

      return new Value(this);
    }
  }
}
