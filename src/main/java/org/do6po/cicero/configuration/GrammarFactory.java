package org.do6po.cicero.configuration;

import java.sql.Driver;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.do6po.cicero.query.grammar.Grammar;
import org.do6po.cicero.query.grammar.PostgresGrammar;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GrammarFactory {
  private static final Map<Class<? extends Driver>, Grammar> grammarMap;

  static {
    grammarMap = new HashMap<>();
    grammarMap.put(org.postgresql.Driver.class, new PostgresGrammar());
  }

  public static Grammar get(Class<? extends Driver> driver) {
    return grammarMap.get(driver);
  }
}
