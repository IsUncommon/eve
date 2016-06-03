package uncmn.eve.store.sql;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper for building selection clauses for {@link android.database.sqlite.SQLiteDatabase}.
 * <p>
 * Each appended clause is combined using {@code AND}. This class is <em>not</em>thread safe.
 * </p>
 */
class SqlQuery {
  private static final String TAG = "SqlQuery.class";

  private String table = null;
  private String[] columns = null;
  private Map<String, String> projectionMap = new HashMap<String, String>();
  private StringBuilder selection = new StringBuilder();
  private ArrayList<String> selectionArgs = new ArrayList<String>();
  private String groupBy = null;
  private String having = null;
  private boolean distinct = true;
  private String orderBy = null;
  private int limit = -1;

  /**
   * Reset any internal state, allowing this builder to be recycled.
   */
  public SqlQuery reset() {
    table = null;
    groupBy = null;
    having = null;
    selection.setLength(0);
    selectionArgs.clear();
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

    if (this.selection.length() > 0) {
      this.selection.append(" AND ");
    }

    this.selection.append("(").append(selection).append(")");
    if (selectionArgs != null) {
      Collections.addAll(this.selectionArgs, selectionArgs);
    }

    return this;
  }

  public SqlQuery groupBy(String groupBy) {
    this.groupBy = groupBy;
    return this;
  }

  public SqlQuery having(String having) {
    this.having = having;
    return this;
  }

  public SqlQuery orderBy(String orderBy) {
    this.orderBy = orderBy;
    return this;
  }

  public SqlQuery distinct(boolean distinct) {
    this.distinct = distinct;
    return this;
  }

  public SqlQuery columns(String[] columns) {
    this.columns = columns;
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
      this.table = sb.toString();
    } else {
      this.table = table;
    }
    return this;
  }

  public SqlQuery table(String table) {
    this.table = table;
    return this;
  }

  private void assertTable() {
    if (table == null) {
      throw new IllegalStateException("Table not specified");
    }
  }

  public SqlQuery mapToTable(String column, String table) {
    projectionMap.put(column, table + "." + column);
    return this;
  }

  public SqlQuery map(String fromColumn, String toClause) {
    projectionMap.put(fromColumn, toClause + " AS " + fromColumn);
    return this;
  }

  public SqlQuery limit(int limit) {
    this.limit = limit;
    return this;
  }

  /**
   * Return selection string for current internal state.
   *
   * @see #getSelectionArgs()
   */
  public String getSelection() {
    return selection.toString();
  }

  /**
   * Return selection arguments for current internal state.
   *
   * @see #getSelection()
   */
  public String[] getSelectionArgs() {
    return selectionArgs.toArray(new String[selectionArgs.size()]);
  }

  private void mapColumns(String[] columns) {
    for (int i = 0; i < columns.length; i++) {
      final String target = projectionMap.get(columns[i]);
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
    for (int i = 0; i < columns.length; i++) {
      String c = columns[i];
      if (i > 0) {
        projection.append(",");
      }
      projection.append(c);
    }
    sql.append("SELECT ").append(projection.toString()).append(" FROM ").append(table);

    if (orderBy != null) {
      sql.append(" ORDERBY ").append(orderBy);
    }

    if (limit != -1) {
      sql.append(" LIMIT ").append(limit);
    }

    return sql;
  }

  @Override public String toString() {
    return "SqlQuery{"
        + "table='"
        + table
        + '\''
        + ", columns="
        + Arrays.toString(columns)
        + ", projectionMap="
        + projectionMap
        + ", selection="
        + selection
        + ", selectionArgs="
        + selectionArgs
        + ", groupBy='"
        + groupBy
        + '\''
        + ", having='"
        + having
        + '\''
        + ", distinct="
        + distinct
        + ", orderBy='"
        + orderBy
        + '\''
        + ", limit="
        + limit
        + '}';
  }
}
