package org.do6po.cicero.configuration;

import org.do6po.cicero.query.grammar.Grammar;
import org.do6po.cicero.query.grammar.PostgresGrammar;

public class GrammarResolver {
  public static Grammar get(SqlDriverEnum driver) {
    return switch (driver) {
      case MYSQL -> null;
      case POSTGRESQL -> new PostgresGrammar();
    };
  }
}
