package org.do6po.cicero.expression.predicate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.do6po.cicero.enums.OperatorEnum;
import org.do6po.cicero.expression.subquery.QueryExpression;

@RequiredArgsConstructor
public class CalculatedPredicateExpression implements PredicateExpression {

  private final QueryExpression queryExpression;
  private final String operator;
  private final List<Object> bindings;
  @Getter private final OperatorEnum type;

  @Override
  public String getExpression() {
    return "(%s) %s (%s)"
        .formatted(
            queryExpression.getSqlExpression().getExpression(),
            operator,
            bindings.stream().map(i -> "?").collect(Collectors.joining(", ")));
  }

  @Override
  public Collection<Object> getBindings() {
    ArrayList<Object> result = new ArrayList<>(queryExpression.getSqlExpression().getBindings());
    result.addAll(bindings);

    return result;
  }
}
