package org.do6po.cicero.expression.write;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.do6po.cicero.expression.predicate.PredicateExpression;

/**
 * Represents a PostgreSQL DELETE statement expression.
 *
 * <p>This class constructs DELETE SQL queries with optional WHERE clauses for PostgreSQL databases.
 * It implements the WriteExpression interface to provide SQL statement generation and parameter
 * binding.
 *
 * <p>Example usage:
 *
 * <pre>
 * // DELETE FROM users WHERE id = ?
 * PredicateExpression where = ...;
 * PostgresDeleteExpression expression = new PostgresDeleteExpression("users", where);
 * String sql = expression.getExpression();
 * </pre>
 *
 * @see WriteExpression
 * @see PredicateExpression
 */
@Getter
@RequiredArgsConstructor
public class PostgresDeleteExpression extends WriteExpression {
  private static final String DELETE_STATEMENT = "delete from %s%s";
  private static final String WHERE_STATEMENT = " where %s";

  /** The name of the table from which records will be deleted. */
  private final String table;

  /** The WHERE clause predicate expression, or null if deleting all records. */
  private final PredicateExpression where;

  /**
   * Constructs the DELETE SQL statement.
   *
   * <p>If a WHERE clause is provided, it generates: {@code DELETE FROM table WHERE condition}
   *
   * <p>If no WHERE clause is provided, it generates: {@code DELETE FROM table}
   *
   * @return the complete DELETE SQL statement
   */
  @Override
  public String getExpression() {
    String whereStatement = hasWhere() ? WHERE_STATEMENT.formatted(where.getExpression()) : "";

    return DELETE_STATEMENT.formatted(table, whereStatement);
  }

  /**
   * Returns the bindings for the WHERE clause.
   *
   * <p>Since DELETE statements don't have column bindings (unlike INSERT/UPDATE), this only
   * returns bindings from the WHERE predicate expression if present.
   *
   * @return collection of parameter bindings for the prepared statement
   */
  @Override
  public Collection<Object> getBindings() {
    if (hasWhere()) {
      return new ArrayList<>(where.getBindings());
    }

    return new ArrayList<>();
  }

  /**
   * Returns an empty list as DELETE statements don't have values to insert/update.
   *
   * @return empty list
   */
  @Override
  List<Map<String, Object>> getValues() {
    return List.of();
  }

  /**
   * Checks if this DELETE statement has a WHERE clause.
   *
   * @return true if a WHERE clause is present and non-empty, false otherwise
   */
  private boolean hasWhere() {
    return where != null && !Objects.equals(where.getExpression(), "");
  }
}
