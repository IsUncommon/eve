package uncmn.eve.store.sql;

import android.content.ContentValues;
import uncmn.eve.Value;

/**
 * Helper class used to create Sql Queries for Key Value table.
 */
public class ValueQuery extends SqlQuery {

  public static final String TABLE = "key_value";
  public static final String KEY = "kv_key";
  public static final String TYPE = "kv_type";
  public static final String VALUE = "kv_value";

  public static final String CREATE_TABLE = "CREATE TABLE "
      + TABLE
      + "("
      + KEY
      + " TEXT,"
      + TYPE
      + " TEXT,"
      + VALUE
      + " BLOB,"
      + "UNIQUE ("
      + KEY
      + ") ON CONFLICT REPLACE)";

  public static final String CREATE_INDEX_TYPE =
      "CREATE INDEX index_type ON " + TABLE + " (" + TYPE + ")";
  public static final String WHERE_KEY = KEY + " = ?";
  public static final String WHERE_TYPE = TYPE + " = ?";

  public static final String[] PROJECTION = {
      KEY, TYPE, VALUE
  };

  public ValueQuery() {
    table(TABLE);
    columns(PROJECTION);
  }

  /**
   * Convert value into content values.
   *
   * @param key key with which value needs to stored.
   * @param value Value instance.
   * @return content values instance.
   */
  public static ContentValues contentValues(String key, Value value) {
    ContentValues values = new ContentValues();
    values.put(KEY, key);
    values.put(TYPE, value.type());
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
}
