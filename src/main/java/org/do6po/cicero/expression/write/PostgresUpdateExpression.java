package org.do6po.cicero.expression.write;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.do6po.cicero.expression.predicate.PredicateExpression;

@Getter
@RequiredArgsConstructor
public class PostgresUpdateExpression extends WriteExpression {
  private static final String UPDATE_STATEMENT = "update %s set %s%s";
  private static final String WHERE_STATEMENT = " where %s";

  private final String table;
  private final List<Map<String, Object>> values;
  private final PredicateExpression where;

  @Override
  public String getExpression() {
    String whereStatement = hasWhere() ? WHERE_STATEMENT.formatted(where.getExpression()) : "";

    return UPDATE_STATEMENT.formatted(table, columnizeUpdate(), whereStatement);
  }

  @Override
  public Collection<Object> getBindings() {
    Collection<Object> result = super.getBindings();

    if (hasWhere()) {
      result.addAll(where.getBindings());
    }

    return result;
  }

  private boolean hasWhere() {
    return where != null && !Objects.equals(where.getExpression(), "");
  }
}
