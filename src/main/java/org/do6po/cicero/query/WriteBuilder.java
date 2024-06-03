package org.do6po.cicero.query;

import java.util.List;
import java.util.Map;

public abstract class WriteBuilder<T, B extends WriteBuilder<T, B>> extends Builder<T, B> {

  public int insert(Map<String, Object> value) {
    return getDbDriver().executeWriteQuery(d -> d.getGrammar().compileInsert(this, List.of(value)));
  }

  public int insert(List<Map<String, Object>> values) {
    return getDbDriver().executeWriteQuery(d -> d.getGrammar().compileInsert(this, values));
  }

  public int update(Map<String, Object> value) {
    return getDbDriver().executeWriteQuery(d -> d.getGrammar().compileUpdate(this, value));
  }
}
