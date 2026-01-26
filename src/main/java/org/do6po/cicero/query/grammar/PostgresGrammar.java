package org.do6po.cicero.query.grammar;

import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.do6po.cicero.expression.Expression;
import org.do6po.cicero.expression.write.PostgresDeleteExpression;
import org.do6po.cicero.expression.write.PostgresInsertExpression;
import org.do6po.cicero.expression.write.PostgresTruncateExpression;
import org.do6po.cicero.expression.write.PostgresUpdateExpression;
import org.do6po.cicero.expression.write.PostgresUpsertExpression;
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

  @Override
  public WriteExpression compileDelete(Builder<?, ?> builder) {
    return new PostgresDeleteExpression(
        wrapTable(builder.getFrom()), builder.getPredicateExpression());
  }

  @Override
  public WriteExpression compileUpsert(
      Builder<?, ?> builder,
      List<Map<String, Object>> values,
      List<String> conflictColumns,
      List<String> updateColumns) {
    String table = wrapTable(builder.getFrom());

    return new PostgresUpsertExpression(table, values, conflictColumns, updateColumns);
  }

  @Override
  public Expression compileTruncate(Builder<?, ?> builder) {
    return new PostgresTruncateExpression(wrapTable(builder.getFrom()));
  }
}
