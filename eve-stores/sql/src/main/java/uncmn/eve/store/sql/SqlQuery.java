package uncmn.eve.store.sql;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper for building selection clauses for {@link android.database.sqlite.SQLiteDatabase}. Each
 * appended clause is combined using {@code AND}. This class is <em>not</em>
 * thread safe.
 */
class SqlQuery {
  private static final String TAG = "SqlQuery.class";

  private String mTable = null;
  private String[] mColumns = null;
  private Map<String, String> mProjectionMap = new HashMap<String, String>();
  private StringBuilder mSelection = new StringBuilder();
  private ArrayList<String> mSelectionArgs = new ArrayList<String>();
  private String mGroupBy = null;
  private String mHaving = null;
  private boolean mDistinct = true;
  private String mOrderBy = null;
  private int mLimit = -1;

  /**
   * Reset any internal state, allowing this builder to be recycled.
   */
  public SqlQuery reset() {
    mTable = null;
    mGroupBy = null;
    mHaving = null;
    mSelection.setLength(0);
    mSelectionArgs.clear();
    return this;
  }

  /**
   * Append the given selection clause to the internal state. Each clause is
   * surrounded with parenthesis and combined using {@code AND}.
   */
  public SqlQuery where(String selection, String... selectionArgs) {
    if (TextUtils.isEmpty(selection)) {
      if (selectionArgs != null && selectionArgs.length > 0) {
        throw new IllegalArgumentException("Valid selection required when including arguments=");
      }

      // Shortcut when clause is empty
      return this;
    }

    if (mSelection.length() > 0) {
      mSelection.append(" AND ");
    }

    mSelection.append("(").append(selection).append(")");
    if (selectionArgs != null) {
      Collections.addAll(mSelectionArgs, selectionArgs);
    }

    return this;
  }

  public SqlQuery groupBy(String groupBy) {
    mGroupBy = groupBy;
    return this;
  }

  public SqlQuery having(String having) {
    mHaving = having;
    return this;
  }

  public SqlQuery orderBy(String orderBy) {
    mOrderBy = orderBy;
    return this;
  }

  public SqlQuery distinct(boolean distinct) {
    mDistinct = distinct;
    return this;
  }

  public SqlQuery table(String table) {
    mTable = table;
    return this;
  }

  public SqlQuery columns(String[] columns) {
    this.mColumns = columns;
    return this;
  }

  /**
   * Replace positional params in table. Use for JOIN ON conditions.
   */
  public SqlQuery table(String table, String... tableParams) {
    if (tableParams != null && tableParams.length > 0) {
      String[] parts = table.split("[?]", tableParams.length + 1);
      StringBuilder sb = new StringBuilder(parts[0]);
      for (int i = 1; i < parts.length; i++) {
        sb.append('"').append(tableParams[i - 1]).append('"').append(parts[i]);
      }
      mTable = sb.toString();
    } else {
      mTable = table;
    }
    return this;
  }

  private void assertTable() {
    if (mTable == null) {
      throw new IllegalStateException("Table not specified");
    }
  }

  public SqlQuery mapToTable(String column, String table) {
    mProjectionMap.put(column, table + "." + column);
    return this;
  }

  public SqlQuery map(String fromColumn, String toClause) {
    mProjectionMap.put(fromColumn, toClause + " AS " + fromColumn);
    return this;
  }

  public SqlQuery limit(int limit) {
    this.mLimit = limit;
    return this;
  }

  /**
   * Return selection string for current internal state.
   *
   * @see #getSelectionArgs()
   */
  public String getSelection() {
    return mSelection.toString();
  }

  /**
   * Return selection arguments for current internal state.
   *
   * @see #getSelection()
   */
  public String[] getSelectionArgs() {
    return mSelectionArgs.toArray(new String[mSelectionArgs.size()]);
  }

  private void mapColumns(String[] columns) {
    for (int i = 0; i < columns.length; i++) {
      final String target = mProjectionMap.get(columns[i]);
      if (target != null) {
        columns[i] = target;
      }
    }
  }

  public String sql() {
    return baseQuery().append(" WHERE ").append(getSelection()).toString();
  }

  public String[] args() {
    return getSelectionArgs();
  }

  public StringBuilder baseQuery() {
    StringBuilder sql = new StringBuilder();
    StringBuilder projection = new StringBuilder();
    for (int i = 0; i < mColumns.length; i++) {
      String c = mColumns[i];
      if (i > 0) {
        projection.append(",");
      }
      projection.append(c);
    }
    sql.append("SELECT ").append(projection.toString()).append(" FROM ").append(mTable);

    if (mOrderBy != null) {
      sql.append(" ORDERBY ").append(mOrderBy);
    }

    if (mLimit != -1) {
      sql.append(" LIMIT ").append(mLimit);
    }

    return sql;
  }

  @Override public String toString() {
    return "SqlQuery[table="
        + mTable
        + ", selection="
        + getSelection()
        + ", selectionArgs="
        + Arrays.toString(getSelectionArgs())
        + "projectionMap = "
        + mProjectionMap
        + " ]";
  }
}
