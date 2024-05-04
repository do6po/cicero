package org.do6po.cicero.expression.select;

import java.util.Collection;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SimpleSelectExpression implements SelectExpression {

  private final Collection<String> columns;

  @Override
  public String getExpression() {
    return String.join(", ", columns);
  }
}
