package org.do6po.cicero.expression.union;

import java.util.Collection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.do6po.cicero.enums.UnionTypeEnum;
import org.do6po.cicero.expression.subquery.QueryExpression;

@RequiredArgsConstructor
public class QueryUnionExpression implements UnionExpression {

  private final QueryExpression queryExpression;
  @Getter private final UnionTypeEnum type;

  @Override
  public String getExpression() {
    return "(%s)".formatted(queryExpression.getSqlExpression().getExpression());
  }

  @Override
  public Collection<Object> getBindings() {
    return queryExpression.getSqlExpression().getBindings();
  }
}
