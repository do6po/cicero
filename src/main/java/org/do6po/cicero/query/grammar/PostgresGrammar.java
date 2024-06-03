package org.do6po.cicero.query.grammar;

import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.do6po.cicero.expression.write.PostgresInsertExpression;
import org.do6po.cicero.expression.write.PostgresUpdateExpression;
import org.do6po.cicero.expression.write.WriteExpression;
import org.do6po.cicero.query.Builder;
import org.do6po.cicero.query.grammar.collector.PgsqlQueryCollector;

@Getter
public class PostgresGrammar extends Grammar {

  private final PgsqlQueryCollector queryCollector = new PgsqlQueryCollector();

  @Override
  public WriteExpression compileInsert(Builder<?, ?> builder, List<Map<String, Object>> values) {
    String table = wrapTable(builder.getFrom());

    if (values == null || values.isEmpty()) {
      return new PostgresInsertExpression(table, List.of());
    }

    return new PostgresInsertExpression(table, values);
  }

  @Override
  public WriteExpression compileUpdate(Builder<?, ?> builder, Map<String, Object> value) {
    return new PostgresUpdateExpression(
        wrapTable(builder.getFrom()), List.of(value), builder.getPredicateExpression());
  }
}
