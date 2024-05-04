package org.do6po.cicero.collector;

import java.util.HashMap;
import java.util.Map;

public class QueryCollectorFactory {

  protected static final Map<String, QueryCollector> COLLECTORS = new HashMap<>();

  static {
    COLLECTORS.put("pgsql", new PgsqlQueryCollector());
  }

  public QueryCollector create() {
    // logic driver for many db
    return COLLECTORS.get("pgsql");
  }
}
