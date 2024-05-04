package org.do6po.cicero.expression.union;

import org.do6po.cicero.enums.UnionTypeEnum;
import org.do6po.cicero.expression.Expression;

public interface UnionExpression extends Expression {
  UnionTypeEnum getType();
}
