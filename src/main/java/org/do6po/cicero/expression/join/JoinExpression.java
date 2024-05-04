package org.do6po.cicero.expression.join;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.do6po.cicero.expression.Expression;

public interface JoinExpression extends Expression {

  JoinTypeEnum getType();

  @Getter
  @RequiredArgsConstructor
  enum JoinTypeEnum {
    INNER_JOIN("INNER JOIN"),
    RIGHT_OUTER("RIGHT OUTER JOIN"),
    LEFT_OUTER("LEFT OUTER JOIN"),
    FULL_OUTER("FULL OUTER JOIN");

    private final String value;
  }
}
