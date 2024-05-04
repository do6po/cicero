package org.do6po.cicero.expression.predicate;

import org.do6po.cicero.enums.OperatorEnum;
import org.do6po.cicero.expression.Expression;

public interface PredicateExpression extends Expression {
  OperatorEnum getType();
}
