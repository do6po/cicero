package org.do6po.cicero.configuration;

import org.do6po.cicero.query.grammar.Grammar;
import org.do6po.cicero.query.grammar.PostgresGrammar;

public class GrammarFactory {
  public static Grammar get(SqlDriverEnum driver) {
    return switch (driver) {
      case POSTGRESQL -> new PostgresGrammar();
    };
  }
}
