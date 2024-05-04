package org.do6po.cicero.expression.predicate;

import lombok.Getter;
import org.do6po.cicero.enums.OperatorEnum;

public class NullPredicateExpression implements PredicateExpression {

  @Getter private final String column;
  private final boolean isNull;
  @Getter private final OperatorEnum type;

  public NullPredicateExpression(String column, boolean isNull, OperatorEnum type) {
    this.column = column;
    this.type = type;
    this.isNull = isNull;
  }

  public NullPredicateExpression(String column, boolean isNull) {
    this(column, isNull, OperatorEnum.AND);
  }

  @Override
  public String getExpression() {
    return "%s %s NULL".formatted(column, isNull ? "IS" : "IS NOT");
  }
}
