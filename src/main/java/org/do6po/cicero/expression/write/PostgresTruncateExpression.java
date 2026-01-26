package org.do6po.cicero.expression.write;

import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.do6po.cicero.expression.Expression;

/**
 * Represents a PostgreSQL TRUNCATE statement expression.
 *
 * <p>The TRUNCATE statement is used to quickly delete all rows from a table. It is more efficient
 * than DELETE for removing all data because it does not scan the table and does not generate
 * individual row delete operations in the transaction log.
 *
 * <p>TRUNCATE provides several advantages over DELETE:
 *
 * <ul>
 *   <li>Faster execution - does not scan the table row by row
 *   <li>Less disk space usage - does not generate WAL (Write-Ahead Log) entries for each row
 *   <li>Automatically resets sequences (auto-increment counters) associated with the table
 * </ul>
 *
 * <p>Example generated SQL:
 *
 * <pre>
 * TRUNCATE TABLE users
 * </pre>
 *
 * <p><strong>Important considerations:</strong>
 *
 * <ul>
 *   <li>TRUNCATE is DDL (Data Definition Language), not DML (Data Manipulation Language)
 *   <li>In PostgreSQL, TRUNCATE can be rolled back within a transaction
 *   <li>Cannot be used if the table is referenced by foreign key constraints (unless CASCADE is
 *       specified)
 *   <li>Does not fire individual row DELETE triggers
 *   <li>Requires TRUNCATE privilege on the table
 * </ul>
 *
 * @see Expression
 */
@RequiredArgsConstructor
public class PostgresTruncateExpression implements Expression {
  private static final String TRUNCATE_STATEMENT = "TRUNCATE TABLE %s";

  private final String table;

  /**
   * Generates the TRUNCATE SQL statement.
   *
   * @return the complete TRUNCATE TABLE statement
   */
  @Override
  public String getExpression() {
    return TRUNCATE_STATEMENT.formatted(table);
  }

  /**
   * Returns empty collection as TRUNCATE does not use parameter bindings.
   *
   * @return empty collection
   */
  @Override
  public Collection<Object> getBindings() {
    return List.of();
  }
}
