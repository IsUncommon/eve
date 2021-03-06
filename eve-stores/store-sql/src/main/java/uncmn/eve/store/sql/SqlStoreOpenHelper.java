package uncmn.eve.store.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * An object instance representing {@link SQLiteOpenHelper} with KeyValue store database.
 */
public class SqlStoreOpenHelper extends SQLiteOpenHelper {

  static final String DB_NAME = "eve_kv_store.db";
  static final int DB_VERSION = 1;

  public SqlStoreOpenHelper(Context context) {
    super(context, DB_NAME, null, DB_VERSION);
  }

  @Override public void onCreate(SQLiteDatabase db) {
    try {
      db.beginTransaction();
      db.execSQL(ValueQuery.CREATE_TABLE);
      db.execSQL(ValueQuery.CREATE_INDEX_TYPE);
      db.execSQL(ValueQuery.CREATE_INDEX_TYPE_KEY);
      db.setTransactionSuccessful();
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      db.endTransaction();
    }
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
  }
}
