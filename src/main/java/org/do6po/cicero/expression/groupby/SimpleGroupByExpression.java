package org.do6po.cicero.expression.groupby;

import java.util.Collection;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SimpleGroupByExpression implements GroupByExpression {

  private final Collection<String> columns;

  @Override
  public String getExpression() {
    return String.join(", ", columns);
  }
}
