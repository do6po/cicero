package org.do6po.cicero.expression.write;

import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostgresInsertExpression extends WriteExpression {
  private static final String INSERT_STATEMENT = "insert into %s (%s) values %s";
  private static final String EMPTY_STATEMENT = "insert into %s default values";

  private final String table;
  private final List<Map<String, Object>> values;

  @Override
  public String getExpression() {
    return values.isEmpty() ? getEmptyStatement() : getInsertStatement();
  }

  private String getEmptyStatement() {
    return EMPTY_STATEMENT.formatted(table);
  }

  private String getInsertStatement() {
    return INSERT_STATEMENT.formatted(table, columnize(), parametrize());
  }
}
