package org.do6po.cicero.expression.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.do6po.cicero.enums.DirectionEnum;

@Getter
@RequiredArgsConstructor
public class SimpleOrderExpression implements OrderExpression {
  private final String column;
  private final DirectionEnum direction;

  public String getExpression() {
    return " %s %s".formatted(column, direction.name());
  }
}
