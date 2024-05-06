package org.do6po.cicero.query.grammar;

import org.do6po.cicero.exception.BaseException;
import org.do6po.cicero.expression.from.FromExpression;
import org.do6po.cicero.expression.write.WriteExpression;
import org.do6po.cicero.query.Builder;

import java.util.List;
import java.util.Map;

public abstract class Grammar {

  public WriteExpression compileInsert(Builder<?, ?> builder, Map<String, Object> value) {
    throw new BaseException("compileInsert is not implemented by given grammar");
  }

  protected String wrapTable(List<FromExpression> from) {
    if (from.isEmpty()) {
      throw new BaseException("Table is not specified");
    }

    if (from.size() != 1) {
      throw new BaseException("More than one tables are specified");
    }

    return wrapTable(from.stream().findFirst().orElseThrow().getExpression());
  }

  protected String wrapTable(String table) {
    return table;
  }
}
