package uncmn.eve;

/**
 * An object instance representing Value.
 * <p> This wraps the value of a key with a type and custom identifiers.</p>
 */
public class Value {

  private byte[] bytes;
  private String type;

  public static Builder builder() {
    return new Builder();
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

  public static class Builder {

    byte[] value;
    String type;

    public Builder value(byte[] value) {
      this.value = value;
      return this;
    }

    public Builder type(String type) {
      this.type = type;
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

      Value v = new Value();
      v.bytes = value;
      v.type = type;
      return v;
    }
  }
}
