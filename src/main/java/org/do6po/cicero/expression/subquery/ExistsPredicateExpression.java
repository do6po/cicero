package org.do6po.cicero.expression.subquery;

import java.util.Collection;
import lombok.Getter;
import org.do6po.cicero.enums.OperatorEnum;
import org.do6po.cicero.expression.predicate.PredicateExpression;

public class ExistsPredicateExpression implements PredicateExpression {

  private final QueryExpression expression;
  @Getter private final boolean reverse;
  @Getter private final OperatorEnum type;

  public ExistsPredicateExpression(QueryExpression expression, boolean reverse, OperatorEnum type) {
    this.expression = expression;
    this.reverse = reverse;
    this.type = type;
  }

  public ExistsPredicateExpression(QueryExpression expression, boolean reverse) {
    this(expression, reverse, OperatorEnum.AND);
  }

  public ExistsPredicateExpression(QueryExpression expression) {
    this(expression, false, OperatorEnum.AND);
  }

  @Override
  public String getExpression() {
    return "%s(%s)"
        .formatted(
            isReverse() ? "NOT EXISTS" : "EXISTS", expression.getSqlExpression().getExpression());
  }

  @Override
  public Collection<Object> getBindings() {
    return expression.getSqlExpression().getBindings();
  }
}
