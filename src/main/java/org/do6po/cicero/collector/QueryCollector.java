package org.do6po.cicero.collector;

import org.do6po.cicero.expression.SqlExpression;
import org.do6po.cicero.query.Builder;

public interface QueryCollector {
  SqlExpression collectExpression(Builder<?, ?> builder);
}
