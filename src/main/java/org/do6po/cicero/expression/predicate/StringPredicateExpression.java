package org.do6po.cicero.expression.predicate;

import java.util.Collection;
import lombok.Getter;
import org.do6po.cicero.enums.OperatorEnum;

@Getter
public class StringPredicateExpression implements PredicateExpression {
  private final String expression;
  private final Collection<Object> bindings;
  private final OperatorEnum type;

  public StringPredicateExpression(
      String expression, Collection<Object> bindings, OperatorEnum type) {
    this.expression = expression;
    this.bindings = bindings;
    this.type = type;
  }
}
