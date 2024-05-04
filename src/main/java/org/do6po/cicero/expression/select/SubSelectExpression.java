package org.do6po.cicero.expression.select;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.do6po.cicero.expression.subquery.QueryExpression;

@RequiredArgsConstructor
public class SubSelectExpression implements SelectExpression {

  private final QueryExpression expression;
  private final String as;

  @Override
  public String getExpression() {
    return "(%s) as %s".formatted(expression.getSqlExpression().getExpression(), as);
  }

  @Override
  public Collection<Object> getBindings() {
    return expression.getSqlExpression().getBindings();
  }
}
