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
}
