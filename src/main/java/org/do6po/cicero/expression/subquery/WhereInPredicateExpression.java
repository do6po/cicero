package org.do6po.cicero.expression.subquery;

import java.util.Collection;
import lombok.Getter;
import org.do6po.cicero.enums.OperatorEnum;
import org.do6po.cicero.expression.predicate.PredicateExpression;

public class WhereInPredicateExpression implements PredicateExpression {

  private final String column;
  private final QueryExpression expression;
  @Getter private final boolean not;
  @Getter private final OperatorEnum type;

  public WhereInPredicateExpression(
      String column, QueryExpression expression, boolean not, OperatorEnum type) {
    this.column = column;
    this.expression = expression;
    this.not = not;
    this.type = type;
  }

  public WhereInPredicateExpression(String column, QueryExpression expression, boolean not) {
    this(column, expression, not, OperatorEnum.AND);
  }

  public WhereInPredicateExpression(String column, QueryExpression expression) {
    this(column, expression, false, OperatorEnum.AND);
  }

  @Override
  public String getExpression() {
    return "%s %s (%s)"
        .formatted(
            column, isNot() ? "NOT IN" : "IN", expression.getSqlExpression().getExpression());
  }

  @Override
  public Collection<Object> getBindings() {
    return expression.getSqlExpression().getBindings();
  }
}
