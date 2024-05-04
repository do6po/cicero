package org.do6po.cicero.expression.predicate;

import java.util.Collection;
import lombok.Getter;
import org.do6po.cicero.enums.OperatorEnum;

public class NestedPredicateExpression implements PredicateExpression {

  private final PredicateExpression predicateExpression;
  @Getter private final OperatorEnum type;

  public NestedPredicateExpression(PredicateExpression predicateExpression, OperatorEnum type) {
    this.predicateExpression = predicateExpression;
    this.type = type;
  }

  @Override
  public String getExpression() {
    return "(%s)".formatted(predicateExpression.getExpression());
  }

  @Override
  public Collection<Object> getBindings() {
    return predicateExpression.getBindings();
  }
}
