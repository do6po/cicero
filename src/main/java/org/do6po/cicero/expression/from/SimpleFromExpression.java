package org.do6po.cicero.expression.from;

import java.util.Objects;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SimpleFromExpression implements FromExpression {
  private final String table;
  private final String as;

  @Override
  public String getExpression() {
    return Objects.isNull(as) ? table : "%s as %s".formatted(table, as);
  }
}
