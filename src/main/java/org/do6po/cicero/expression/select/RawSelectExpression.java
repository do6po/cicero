package org.do6po.cicero.expression.select;

import java.util.Collection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RawSelectExpression implements SelectExpression {
  private final String raw;
  @Getter private final Collection<Object> bindings;

  @Override
  public String getExpression() {
    return raw;
  }
}
