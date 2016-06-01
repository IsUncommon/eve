package uncmn.eve;

import java.lang.reflect.Type;

/**
 * An object instance representing Value.
 * <p> This wraps the value of a key with a type and custom identifiers.</p>
 */
public class Value {

  private byte[] bytes;
  private String type;
  private boolean isPrimitive;

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

  public void isPrimitive(boolean isPrimitive) {
    this.isPrimitive = isPrimitive;
  }

  public boolean isPrimitive() {
    return isPrimitive;
  }

  public static class Builder {

    byte[] value;
    Type type;
    boolean isPrimitive;

    public Builder value(byte[] value) {
      this.value = value;
      return this;
    }

    public Builder type(Class type) {
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
        throw new IllegalArgumentException("Type cannot be null");
      }

      Value v = new Value();
      v.bytes = value;
      v.isPrimitive = Types.isPrimitive(type);
      v.type = Types.typeToString(type);
      return v;
    }
  }
}
