package org.do6po.cicero.query.grammar;

import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.do6po.cicero.expression.write.PostgresInsertExpression;
import org.do6po.cicero.expression.write.WriteExpression;
import org.do6po.cicero.query.Builder;
import org.do6po.cicero.query.grammar.collector.PgsqlQueryCollector;

@Getter
public class PostgresGrammar extends Grammar {

  private final PgsqlQueryCollector queryCollector = new PgsqlQueryCollector();

  @Override
  public WriteExpression compileInsert(Builder<?, ?> builder, Map<String, Object> value) {
    String table = wrapTable(builder.getFrom());

    if (value == null || value.isEmpty()) {
      return new PostgresInsertExpression(table, List.of());
    }

    return new PostgresInsertExpression(table, List.of(value));
  }
}
