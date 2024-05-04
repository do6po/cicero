package org.do6po.cicero.expression;

import java.util.Collection;
import java.util.List;

public interface Expression {

  String getExpression();

  default Collection<Object> getBindings() {
    return List.of();
  }
}
