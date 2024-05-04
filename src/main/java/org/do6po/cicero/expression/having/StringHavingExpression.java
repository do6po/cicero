package org.do6po.cicero.expression.having;

import java.util.Collection;
import lombok.Getter;
import org.do6po.cicero.enums.OperatorEnum;

public class StringHavingExpression implements HavingExpression {
  private final String string;
  @Getter private final Collection<Object> bindings;

  @Getter private final OperatorEnum type;

  public StringHavingExpression(String string, Collection<Object> bindings, OperatorEnum type) {
    this.string = string;
    this.bindings = bindings;
    this.type = type;
  }

  @Override
  public String getExpression() {
    return string;
  }
}
