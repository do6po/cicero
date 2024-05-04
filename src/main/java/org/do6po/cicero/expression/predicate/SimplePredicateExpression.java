package org.do6po.cicero.expression.predicate;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import org.do6po.cicero.enums.OperatorEnum;

@Getter
public class SimplePredicateExpression implements PredicateExpression {
  private final String column;
  private final String operator;
  private final Collection<Object> bindings;
  private final OperatorEnum type;

  public SimplePredicateExpression(
      String column, String operator, Collection<Object> bindings, OperatorEnum type) {
    this.column = column;
    this.operator = operator;
    this.bindings = bindings;
    this.type = type;
  }

  public SimplePredicateExpression(String column, String operator, Collection<Object> bindings) {
    this(column, operator, bindings, OperatorEnum.AND);
  }

  public SimplePredicateExpression(
      String column, String operator, Object binding, OperatorEnum type) {
    this(column, operator, List.of(binding), type);
  }

  public SimplePredicateExpression(String column, String operator, Object binding) {
    this(column, operator, binding, OperatorEnum.AND);
  }

  public String getExpression() {
    return "%s %s (%s)"
        .formatted(
            column, operator, bindings.stream().map(i -> "?").collect(Collectors.joining(", ")));
  }
}
