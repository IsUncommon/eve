package uncmn.eve.store.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import java.util.ArrayList;
import java.util.List;
import rx.schedulers.Schedulers;
import uncmn.eve.Converter;
import uncmn.eve.Entry;
import uncmn.eve.Store;
import uncmn.eve.Value;

/**
 * An implementation of {@link Store} with Sqlite operations.
 */
public class SqlStore extends Store {

  BriteDatabase db;
  String dbPath;
  String dbName;

  SqlStore(Context context, Converter converter) {
    super(converter);

    SqlStoreOpenHelper openHelper = new SqlStoreOpenHelper(context);

    db = SqlBrite.create(new SqlBrite.Logger() {
      @Override public void log(String message) {

      }
    }).wrapDatabaseHelper(openHelper, Schedulers.io());
    this.dbPath = openHelper.getReadableDatabase().getPath();
    this.dbName = openHelper.getDatabaseName();
  }

  public static SqlStore create(Context context, Converter converter) {
    return new SqlStore(context, converter);
  }

  /**
   * Get database path.
   */
  public String dbPath() {
    return dbPath;
  }

  /**
   * Get database name.
   */
  public String dbName() {
    return dbName;
  }

  /**
   * Get number of entries in the database.
   */
  public int count() {
    final String sql = "select count(*) FROM " + ValueQuery.TABLE;

    Cursor cursor = db.query(sql);
    int count = 0;
    try {
      if (cursor != null && cursor.getCount() > 0) {
        cursor.moveToFirst();
        count = cursor.getInt(0);
      }
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
    return count;
  }

  @Override public void set(String key, Value value) {
    ValueQuery query = ValueQuery.queryBuilder().key(key);

    final String sql = query.sql();
    final String[] args = query.args();

    Cursor cursor = db.query(sql, args);
    ContentValues contentValues = ValueQuery.contentValues(key, value);
    try {
      if (cursor != null && cursor.getCount() > 0) {
        db.update(ValueQuery.TABLE, contentValues, SQLiteDatabase.CONFLICT_REPLACE,
            ValueQuery.WHERE_KEY, args);
      } else {
        db.insert(ValueQuery.TABLE, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
      }
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
  }

  @Override public Class type(String key) {
    ValueQuery query = ValueQuery.queryBuilder().key(key);

    final String sql = query.sql();
    final String[] args = query.args();
    String type = null;
    Cursor cursor = db.query(sql, args);
    try {
      if (cursor != null && cursor.getCount() > 0) {
        cursor.moveToFirst();
        type = cursor.getString(cursor.getColumnIndexOrThrow(ValueQuery.TYPE));
      }
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
    return converterType(type);
  }

  @Override @SuppressWarnings("unchecked") public <T> T get(String key) {
    ValueQuery query = ValueQuery.queryBuilder().key(key);
    final String sql = query.sql();
    final String[] args = query.args();

    Cursor cursor = db.query(sql, args);
    try {
      if (cursor != null && cursor.getCount() > 0) {
        cursor.moveToFirst();
        String type = cursor.getString(cursor.getColumnIndexOrThrow(ValueQuery.TYPE));
        byte[] value = cursor.getBlob(cursor.getColumnIndexOrThrow(ValueQuery.VALUE));
        return (T) convert(value, type);
      }
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }

    return null;
  }

  /**
   * Clear database and return number of rows deleted.
   */
  @Override public int clear() {
    return db.delete(ValueQuery.TABLE, null);
  }

  @Override public boolean delete(String key) {
    ValueQuery query = ValueQuery.queryBuilder().key(key);

    final String sql = query.sql();
    final String[] args = query.args();
    Cursor cursor = db.query(sql, args);
    try {
      if (cursor != null && cursor.getCount() > 0) {
        db.delete(ValueQuery.TABLE, ValueQuery.WHERE_KEY, args);
        return true;
      }
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
    return false;
  }

  @Override public boolean exists(String key) {
    ValueQuery query = ValueQuery.queryBuilder().key(key);

    final String sql = query.sql();
    final String[] args = query.args();
    Cursor cursor = db.query(sql, args);

    try {
      if (cursor != null && cursor.getCount() > 0) {
        cursor.moveToFirst();
        return true;
      }
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
    return false;
  }

  @Override
  protected <T> List<Entry<T>> entriesKeyContains(String converterKey, String keyContains) {
    ValueQuery query;

    if (converterKey != null && converterKey.length() > 0) {
      query = ValueQuery.queryBuilder().keyContains(keyContains).type(converterKey);
    } else {
      query = ValueQuery.queryBuilder().keyContains(keyContains);
    }
    final String sql = query.sql();
    final String[] args = query.args();
    ArrayList<Entry<T>> entries = new ArrayList<>();
    Cursor cursor = db.query(sql, args);
    try {
      if (cursor != null) {
        while (cursor.moveToNext()) {
          String type = cursor.getString(cursor.getColumnIndexOrThrow(ValueQuery.TYPE));
          String key = cursor.getString(cursor.getColumnIndexOrThrow(ValueQuery.KEY));
          byte[] value = cursor.getBlob(cursor.getColumnIndexOrThrow(ValueQuery.VALUE));
          T val = convert(value, type);
          entries.add(Entry.create(key, val));
        }
      }
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
    return entries;
  }

  @Override protected <T> List<Entry<T>> entriesKeyPrefix(String converterKey, String keyPrefix) {
    ValueQuery query;

    if (converterKey != null && converterKey.length() > 0) {
      query = ValueQuery.queryBuilder().keyPrefix(keyPrefix).type(converterKey);
    } else {
      query = ValueQuery.queryBuilder().keyPrefix(keyPrefix);
    }
    final String sql = query.sql();
    final String[] args = query.args();
    ArrayList<Entry<T>> entries = new ArrayList<>();
    Cursor cursor = db.query(sql, args);
    try {
      if (cursor != null) {
        while (cursor.moveToNext()) {
          String type = cursor.getString(cursor.getColumnIndexOrThrow(ValueQuery.TYPE));
          String key = cursor.getString(cursor.getColumnIndexOrThrow(ValueQuery.KEY));
          byte[] value = cursor.getBlob(cursor.getColumnIndexOrThrow(ValueQuery.VALUE));
          T val = convert(value, type);
          entries.add(Entry.create(key, val));
        }
      }
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
    return entries;
  }

  @Override protected <T> List<Entry<T>> entries(String converterKey) {
    ValueQuery query = ValueQuery.queryBuilder().type(converterKey);
    final String sql = query.sql();
    final String[] args = query.args();
    ArrayList<Entry<T>> entries = new ArrayList<>();
    Cursor cursor = db.query(sql, args);
    try {
      if (cursor != null) {
        while (cursor.moveToNext()) {
          String type = cursor.getString(cursor.getColumnIndexOrThrow(ValueQuery.TYPE));
          String key = cursor.getString(cursor.getColumnIndexOrThrow(ValueQuery.KEY));
          byte[] value = cursor.getBlob(cursor.getColumnIndexOrThrow(ValueQuery.VALUE));
          T val = convert(value, type);
          entries.add(Entry.create(key, val));
        }
      }
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
    return entries;
  }

  @Override protected List<String> keysType(String converterKey) {
    ValueQuery query = ValueQuery.queryBuilder().type(converterKey);
    final String sql = query.sql();
    final String[] args = query.args();
    ArrayList<String> keys = new ArrayList<>();
    Cursor cursor = db.query(sql, args);
    try {
      if (cursor != null) {
        while (cursor.moveToNext()) {
          String key = cursor.getString(cursor.getColumnIndexOrThrow(ValueQuery.KEY));
          keys.add(key);
        }
      }
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
    return keys;
  }

  @Override protected List<String> keysPrefixAny(String keyPrefix) {
    return keysPrefix(null, keyPrefix);
  }

  @Override protected List<String> keysContainsAny(String keyContains) {
    return keysContains(null, keyContains);
  }

  @Override protected List<String> keysPrefix(String converterKey, String keyPrefix) {
    ValueQuery query;

    if (converterKey != null && converterKey.length() > 0) {
      query = ValueQuery.queryBuilder().keyPrefix(keyPrefix).type(converterKey);
    } else {
      query = ValueQuery.queryBuilder().keyPrefix(keyPrefix);
    }

    final String sql = query.sql();
    final String[] args = query.args();
    ArrayList<String> keys = new ArrayList<>();
    Cursor cursor = db.query(sql, args);
    try {
      if (cursor != null) {
        while (cursor.moveToNext()) {
          String key = cursor.getString(cursor.getColumnIndexOrThrow(ValueQuery.KEY));
          keys.add(key);
        }
      }
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
    return keys;
  }

  @Override protected List<String> keysContains(String converterKey, String keyContains) {
    ValueQuery query;

    if (converterKey != null && converterKey.length() > 0) {
      query = ValueQuery.queryBuilder().keyContains(keyContains).type(converterKey);
    } else {
      query = ValueQuery.queryBuilder().keyContains(keyContains);
    }

    final String sql = query.sql();
    final String[] args = query.args();
    ArrayList<String> keys = new ArrayList<>();
    Cursor cursor = db.query(sql, args);
    try {
      if (cursor != null) {
        while (cursor.moveToNext()) {
          String key = cursor.getString(cursor.getColumnIndexOrThrow(ValueQuery.KEY));
          keys.add(key);
        }
      }
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
    return keys;
  }

  @Override protected <T> List<T> valuesType(String converterKey) {
    ValueQuery query = ValueQuery.queryBuilder().type(converterKey);

    final String sql = query.sql();
    final String[] args = query.args();
    ArrayList<T> values = new ArrayList<>();
    Cursor cursor = db.query(sql, args);
    try {
      if (cursor != null) {
        while (cursor.moveToNext()) {
          String type = cursor.getString(cursor.getColumnIndexOrThrow(ValueQuery.TYPE));
          byte[] value = cursor.getBlob(cursor.getColumnIndexOrThrow(ValueQuery.VALUE));
          T val = convert(value, type);
          values.add(val);
        }
      }
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
    return values;
  }

  @Override protected <T> List<T> valuesPrefix(String converterKey, String keyPrefix) {
    ValueQuery query;

    if (converterKey != null && converterKey.length() > 0) {
      query = ValueQuery.queryBuilder().keyPrefix(keyPrefix).type(converterKey);
    } else {
      query = ValueQuery.queryBuilder().keyPrefix(keyPrefix);
    }
    final String sql = query.sql();
    final String[] args = query.args();
    ArrayList<T> values = new ArrayList<>();
    Cursor cursor = db.query(sql, args);
    try {
      if (cursor != null) {
        while (cursor.moveToNext()) {
          String type = cursor.getString(cursor.getColumnIndexOrThrow(ValueQuery.TYPE));
          byte[] value = cursor.getBlob(cursor.getColumnIndexOrThrow(ValueQuery.VALUE));
          T val = convert(value, type);
          values.add(val);
        }
      }
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
    return values;
  }

  @Override protected <T> List<T> valuesContains(String converterKey, String keyContains) {
    ValueQuery query;

    if (converterKey != null && converterKey.length() > 0) {
      query = ValueQuery.queryBuilder().keyContains(keyContains).type(converterKey);
    } else {
      query = ValueQuery.queryBuilder().keyContains(keyContains);
    }

    final String sql = query.sql();
    final String[] args = query.args();
    ArrayList<T> values = new ArrayList<>();
    Cursor cursor = db.query(sql, args);
    try {
      if (cursor != null) {
        while (cursor.moveToNext()) {
          String type = cursor.getString(cursor.getColumnIndexOrThrow(ValueQuery.TYPE));
          byte[] value = cursor.getBlob(cursor.getColumnIndexOrThrow(ValueQuery.VALUE));
          T val = convert(value, type);
          values.add(val);
        }
      }
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
    return values;
  }

  @Override protected List<Object> valuesPrefixAny(String keyPrefix) {
    return valuesPrefix(null, keyPrefix);
  }

  @Override protected List<Object> valuesContainsAny(String keyContains) {
    return valuesContains(null, keyContains);
  }
}
