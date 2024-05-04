package org.do6po.cicero.expression;

import java.util.Collection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SimpleSqlExpression implements SqlExpression {
  private final String expression;
  private final Collection<Object> bindings;
}
