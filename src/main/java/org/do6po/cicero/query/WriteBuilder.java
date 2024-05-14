package org.do6po.cicero.query;

import java.util.Map;

public abstract class WriteBuilder<T, B extends WriteBuilder<T, B>> extends Builder<T, B> {

  public int insert(Map<String, Object> value) {
    return getDbDriver().executeWriteQuery(d -> d.getGrammar().compileInsert(this, value));
  }
}
