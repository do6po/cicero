package org.do6po.cicero.expression.from;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.do6po.cicero.expression.subquery.QueryExpression;

@RequiredArgsConstructor
public class QueryFromExpression implements FromExpression {
  private final QueryExpression queryExpression;
  private final String as;

  @Override
  public String getExpression() {
    return "(%s) as %s".formatted(queryExpression.getSqlExpression().getExpression(), as);
  }

  @Override
  public Collection<Object> getBindings() {
    return queryExpression.getSqlExpression().getBindings();
  }
}
