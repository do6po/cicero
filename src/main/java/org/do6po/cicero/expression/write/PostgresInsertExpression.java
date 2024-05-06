package org.do6po.cicero.expression.write;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class PostgresInsertExpression implements WriteExpression {
  private static final String INSERT_STATEMENT = "insert into %s (%s) values %s";
  private static final String EMPTY_STATEMENT = "insert into %s default values";

  @Getter private final String table;
  @Getter private final List<Map<String, Object>> values;

  @Override
  public String getExpression() {
    return values.isEmpty() ? getEmptyStatement() : getInsertStatement(INSERT_STATEMENT);
  }

  protected String getEmptyStatement() {
    return EMPTY_STATEMENT.formatted(table);
  }
}
