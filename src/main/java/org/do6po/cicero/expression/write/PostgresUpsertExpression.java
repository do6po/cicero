package org.do6po.cicero.expression.write;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Represents a PostgreSQL UPSERT (INSERT ... ON CONFLICT) statement expression.
 *
 * <p>This class constructs UPSERT SQL queries that insert new records or update existing ones when
 * a conflict occurs on specified columns (typically unique constraints or primary keys).
 *
 * <p>PostgreSQL syntax: {@code INSERT INTO table (columns) VALUES (?, ?) ON CONFLICT (conflict_columns) DO UPDATE SET column = EXCLUDED.column}
 *
 * <p>Example usage:
 *
 * <pre>
 * // Upsert user by id
 * List&lt;Map&lt;String, Object&gt;&gt; values = List.of(
 *     Map.of("id", 1, "username", "john", "email", "john@example.com")
 * );
 * PostgresUpsertExpression expression = new PostgresUpsertExpression(
 *     "users", values, List.of("id"), List.of("username", "email")
 * );
 * // Result: INSERT INTO users (id, username, email) VALUES (?, ?, ?)
 * //         ON CONFLICT (id) DO UPDATE SET username = EXCLUDED.username, email = EXCLUDED.email
 * </pre>
 *
 * @see WriteExpression
 * @see PostgresInsertExpression
 */
@Getter
@RequiredArgsConstructor
public class PostgresUpsertExpression extends WriteExpression {
  private static final String UPSERT_STATEMENT =
      "insert into %s (%s) values %s on conflict (%s) do update set %s";
  private static final String UPSERT_NOTHING_STATEMENT =
      "insert into %s (%s) values %s on conflict (%s) do nothing";

  /** The name of the table where records will be upserted. */
  private final String table;

  /** The list of value maps to insert/update. */
  private final List<Map<String, Object>> values;

  /**
   * The columns that define the conflict target (e.g., primary key or unique constraint columns).
   */
  private final List<String> conflictColumns;

  /**
   * The columns to update when a conflict occurs. If null or empty, DO NOTHING will be used.
   */
  private final List<String> updateColumns;

  /**
   * Constructs the UPSERT SQL statement.
   *
   * <p>If updateColumns is empty or null, generates: {@code INSERT INTO table (...) VALUES (...)
   * ON CONFLICT (...) DO NOTHING}
   *
   * <p>Otherwise generates: {@code INSERT INTO table (...) VALUES (...) ON CONFLICT (...) DO
   * UPDATE SET ...}
   *
   * @return the complete UPSERT SQL statement
   */
  @Override
  public String getExpression() {
    if (updateColumns == null || updateColumns.isEmpty()) {
      return UPSERT_NOTHING_STATEMENT.formatted(
          table, columnize(), parametrize(), columnizeConflict());
    }

    return UPSERT_STATEMENT.formatted(
        table, columnize(), parametrize(), columnizeConflict(), columnizeUpdate());
  }

  /**
   * Returns the conflict columns as a comma-separated string.
   *
   * @return conflict columns string (e.g., "id" or "id, email")
   */
  protected String columnizeConflict() {
    return String.join(", ", conflictColumns);
  }

  /**
   * Returns the UPDATE SET clause for conflict resolution.
   *
   * <p>Generates: {@code column1 = EXCLUDED.column1, column2 = EXCLUDED.column2, ...}
   *
   * @return the UPDATE SET clause
   */
  protected String columnizeUpdate() {
    if (updateColumns == null || updateColumns.isEmpty()) {
      return "";
    }

    return updateColumns.stream()
        .map(col -> "%s = EXCLUDED.%s".formatted(col, col))
        .collect(Collectors.joining(", "));
  }

  /**
   * Returns all columns from the first value map, excluding conflict columns if they shouldn't be
   * updated.
   *
   * <p>Note: This override ensures we get all columns for the INSERT part, not just update
   * columns.
   *
   * @return set of column names
   */
  @Override
  protected Set<String> getColumns() {
    return values.stream().findFirst().orElseThrow().keySet();
  }

  /**
   * Returns bindings for the INSERT values only.
   *
   * <p>The UPDATE part uses EXCLUDED.column references, so no additional bindings are needed.
   *
   * @return collection of parameter bindings for the prepared statement
   */
  @Override
  public Collection<Object> getBindings() {
    return super.getBindings();
  }
}
