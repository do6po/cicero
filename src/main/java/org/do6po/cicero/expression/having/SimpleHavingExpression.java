package org.do6po.cicero.expression.having;

import java.util.Collection;
import java.util.stream.Collectors;
import lombok.Getter;
import org.do6po.cicero.enums.OperatorEnum;

public class SimpleHavingExpression implements HavingExpression {
  private final String column;
  private final String operator;
  @Getter private final OperatorEnum type;
  @Getter private final Collection<Object> bindings;

  public SimpleHavingExpression(
      String column, String operator, Collection<Object> bindings, OperatorEnum type) {
    this.column = column;
    this.operator = operator;
    this.type = type;
    this.bindings = bindings;
  }

  public SimpleHavingExpression(String column, String operator, Collection<Object> bindings) {
    this(column, operator, bindings, OperatorEnum.AND);
  }

  @Override
  public String getExpression() {
    return "%s %s (%s)"
        .formatted(
            column, operator, bindings.stream().map(i -> "?").collect(Collectors.joining(", ")));
  }
}
