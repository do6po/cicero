package org.do6po.cicero.expression.having;

import org.do6po.cicero.enums.OperatorEnum;
import org.do6po.cicero.expression.Expression;

public interface HavingExpression extends Expression {
  OperatorEnum getType();
}
