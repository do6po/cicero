package org.do6po.cicero.expression.predicate;

import lombok.Getter;
import org.do6po.cicero.enums.OperatorEnum;

public class ColumnPredicateExpression implements PredicateExpression {

  private final String first;
  private final String second;
  private final String operator;

  @Getter private final OperatorEnum type;

  public ColumnPredicateExpression(
      String first, String operator, String second, OperatorEnum type) {
    this.first = first;
    this.second = second;
    this.operator = operator;
    this.type = type;
  }

  @Override
  public String getExpression() {
    return "%s %s %s".formatted(first, operator, second);
  }
}
