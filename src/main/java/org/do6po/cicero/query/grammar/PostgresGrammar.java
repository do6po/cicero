package org.do6po.cicero.query.grammar;

import org.do6po.cicero.expression.write.PostgresInsertExpression;
import org.do6po.cicero.expression.write.WriteExpression;
import org.do6po.cicero.query.Builder;

import java.util.List;
import java.util.Map;

public class PostgresGrammar extends Grammar {
  @Override
  public WriteExpression compileInsert(Builder<?, ?> builder, Map<String, Object> value) {
    String table = wrapTable(builder.getFrom());

    if (value == null || value.isEmpty()) {
      return new PostgresInsertExpression(table, List.of());
    }

    return new PostgresInsertExpression(table, List.of(value));
  }
}
