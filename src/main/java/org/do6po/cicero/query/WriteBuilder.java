package org.do6po.cicero.query;

import java.util.List;
import java.util.Map;

/**
 * Abstract builder for write operations (INSERT, UPDATE, DELETE) on database tables.
 *
 * <p>This class extends {@link Builder} and provides methods for modifying data in the database. It
 * supports inserting, updating, and deleting records with various conditions and parameters.
 *
 * @param <T> the type of objects returned by query execution
 * @param <B> the type of the builder implementation (for fluent API support)
 * @see Builder
 * @see PlainWriteBuilder
 */
public abstract class WriteBuilder<T, B extends WriteBuilder<T, B>> extends Builder<T, B> {

  /**
   * Inserts a single record into the database table.
   *
   * <p>Example usage:
   *
   * <pre>
   * Map&lt;String, Object&gt; values = new HashMap&lt;&gt;();
   * values.put("name", "John");
   * values.put("email", "john@example.com");
   * int rowsInserted = builder.insert(values);
   * </pre>
   *
   * @param value a map containing column names as keys and their values to insert
   * @return the number of rows inserted (typically 1)
   */
  public int insert(Map<String, Object> value) {
    return getDbDriver().executeWriteQuery(d -> d.getGrammar().compileInsert(this, List.of(value)));
  }

  /**
   * Inserts multiple records into the database table in a single query.
   *
   * <p>This is more efficient than calling {@link #insert(Map)} multiple times as it executes a
   * single batch INSERT statement.
   *
   * <p>Example usage:
   *
   * <pre>
   * List&lt;Map&lt;String, Object&gt;&gt; records = List.of(
   *     Map.of("name", "John", "email", "john@example.com"),
   *     Map.of("name", "Jane", "email", "jane@example.com")
   * );
   * int rowsInserted = builder.insert(records);
   * </pre>
   *
   * @param values a list of maps, each containing column names and values to insert
   * @return the number of rows inserted
   */
  public int insert(List<Map<String, Object>> values) {
    return getDbDriver().executeWriteQuery(d -> d.getGrammar().compileInsert(this, values));
  }

  /**
   * Updates records in the database table that match the builder's WHERE conditions.
   *
   * <p>Example usage:
   *
   * <pre>
   * Map&lt;String, Object&gt; updates = new HashMap&lt;&gt;();
   * updates.put("status", "active");
   * updates.put("updated_at", Timestamp.from(Instant.now()));
   *
   * int rowsUpdated = builder
   *     .where("id", 1)
   *     .update(updates);
   * </pre>
   *
   * @param value a map containing column names as keys and their new values
   * @return the number of rows updated
   */
  public int update(Map<String, Object> value) {
    return getDbDriver().executeWriteQuery(d -> d.getGrammar().compileUpdate(this, value));
  }

  /**
   * Deletes records from the database table that match the builder's WHERE conditions.
   *
   * <p><strong>WARNING:</strong> If no WHERE conditions are specified, this will delete ALL records
   * from the table.
   *
   * <p>Example usage:
   *
   * <pre>
   * // Delete specific records
   * int rowsDeleted = builder
   *     .where("status", "inactive")
   *     .delete();
   *
   * // Delete all records (use with caution!)
   * int rowsDeleted = builder.delete();
   * </pre>
   *
   * @return the number of rows deleted
   */
  public int delete() {
    return getDbDriver().executeWriteQuery(d -> d.getGrammar().compileDelete(this));
  }

  /**
   * Inserts records or updates them if they already exist (UPSERT operation).
   *
   * <p>Uses PostgreSQL's ON CONFLICT clause to handle conflicts. When a conflict occurs on the
   * specified columns, the record will be updated instead of causing an error.
   *
   * <p>Example usage:
   *
   * <pre>
   * Map&lt;String, Object&gt; values = Map.of(
   *     "id", 1,
   *     "username", "john",
   *     "email", "john@example.com"
   * );
   *
   * // Update username and email if id already exists
   * int affected = builder.upsert(
   *     values,
   *     List.of("id"),                    // conflict column (primary key)
   *     List.of("username", "email")      // columns to update on conflict
   * );
   * </pre>
   *
   * @param value a map containing column names as keys and their values
   * @param conflictColumns columns that define the conflict target (e.g., primary key or unique
   *     constraint)
   * @param updateColumns columns to update when a conflict occurs; if empty or null, DO NOTHING is
   *     used
   * @return the number of rows inserted or updated
   */
  public int upsert(
      Map<String, Object> value, List<String> conflictColumns, List<String> updateColumns) {
    return upsert(List.of(value), conflictColumns, updateColumns);
  }

  /**
   * Inserts multiple records or updates them if they already exist (batch UPSERT operation).
   *
   * <p>More efficient than calling {@link #upsert(Map, List, List)} multiple times as it executes
   * a single batch UPSERT statement.
   *
   * <p>Example usage:
   *
   * <pre>
   * List&lt;Map&lt;String, Object&gt;&gt; records = List.of(
   *     Map.of("id", 1, "username", "john", "email", "john@example.com"),
   *     Map.of("id", 2, "username", "jane", "email", "jane@example.com")
   * );
   *
   * int affected = builder.upsert(
   *     records,
   *     List.of("id"),
   *     List.of("username", "email")
   * );
   * </pre>
   *
   * @param values a list of maps, each containing column names and values
   * @param conflictColumns columns that define the conflict target
   * @param updateColumns columns to update when a conflict occurs; if empty or null, DO NOTHING is
   *     used
   * @return the number of rows inserted or updated
   */
  public int upsert(
      List<Map<String, Object>> values, List<String> conflictColumns, List<String> updateColumns) {
    return getDbDriver()
        .executeWriteQuery(
            d -> d.getGrammar().compileUpsert(this, values, conflictColumns, updateColumns));
  }

  /**
   * Truncates the table by removing all records.
   *
   * <p>The TRUNCATE operation is much faster than DELETE for removing all records because it does
   * not scan the table or generate individual row delete operations. However, it has some important
   * characteristics:
   *
   * <ul>
   *   <li>Resets auto-increment counters to their initial value
   *   <li>Cannot be rolled back in some database configurations
   *   <li>Does not fire DELETE triggers
   *   <li>Requires elevated privileges (typically table owner or superuser)
   * </ul>
   *
   * <p><strong>WARNING:</strong> This operation removes ALL data from the table. Use with extreme
   * caution, especially in production environments.
   *
   * <p>Example usage:
   *
   * <pre>
   * // Remove all records from the users table
   * int affected = PlainWriteBuilder.query()
   *     .from("temp_users")
   *     .truncate();
   * </pre>
   *
   * @return always returns 0 (truncate does not return affected row count)
   */
  public int truncate() {
    return getDbDriver().executeWriteQuery(d -> d.getGrammar().compileTruncate(this));
  }
}
