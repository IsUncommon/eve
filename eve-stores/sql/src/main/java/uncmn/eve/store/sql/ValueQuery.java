package uncmn.eve.store.sql;

import android.content.ContentValues;
import java.lang.reflect.Type;
import uncmn.eve.Types;
import uncmn.eve.Value;

/**
 * Helper class used to create Sql Queries for Key Value table.
 */
public class ValueQuery extends SqlQuery {

  public static final String TABLE = "key_value";
  public static final String KEY = "kv_key";
  public static final String TYPE = "kv_type";
  public static final String IS_PRIMITIVE = "kv_is_primitive";
  public static final String VALUE = "kv_value";

  public static final String CREATE_TABLE = "CREATE TABLE "
      + TABLE
      + "("
      + KEY
      + " TEXT,"
      + TYPE
      + " TEXT,"
      + IS_PRIMITIVE
      + " INTEGER,"
      + VALUE
      + " BLOB,"
      + "UNIQUE ("
      + KEY
      + ") ON CONFLICT REPLACE)";

  public static final String WHERE_KEY = KEY + " = ?";
  public static final String WHERE_TYPE = TYPE + " = ?";
  public static final String WHERE_PRIMITIVE = IS_PRIMITIVE + " = ?";

  public static final String[] PROJECTION = {
      KEY, TYPE, IS_PRIMITIVE, VALUE
  };

  public ValueQuery() {
    table(TABLE);
    columns(PROJECTION);
  }

  public static ContentValues contentValues(String key, Value value) {
    ContentValues values = new ContentValues();
    values.put(KEY, key);
    values.put(TYPE, value.type());
    values.put(IS_PRIMITIVE, value.isPrimitive());
    values.put(VALUE, value.bytes());
    return values;
  }

  public static ValueQuery queryBuilder() {
    return new ValueQuery();
  }

  public ValueQuery key(String key) {
    where(WHERE_KEY, key);
    return this;
  }

  public ValueQuery type(String type) {
    where(WHERE_TYPE, type);
    return this;
  }

  public ValueQuery type(Type type) {
    where(WHERE_TYPE, Types.typeToString(type));
    return this;
  }
}
