package org.do6po.cicero.test.unit.query;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import org.do6po.cicero.component.DbDriverResolverContainer;
import org.do6po.cicero.configuration.DbDriver;
import org.do6po.cicero.configuration.DbDriverResolver;
import org.do6po.cicero.query.grammar.Grammar;
import org.do6po.cicero.query.grammar.collector.PgsqlQueryCollector;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public abstract class BaseQueryTest {
  protected static final DbDriverResolver resolver = mock(DbDriverResolver.class);
  protected static final DbDriver dbDriver = mock(DbDriver.class);
  protected static final Grammar grammar = mock(Grammar.class);
  protected static final PgsqlQueryCollector pgsqlQueryCollector = new PgsqlQueryCollector();

  public static final String table1 = "table1";
  public static final String table2 = "table2";
  public static final String table3 = "table3";

  public static final String column1 = "column1";
  public static final String column2 = "column2";
  public static final String column3 = "column3";

  public static final String value1 = "Some text";
  public static final Integer value2 = 100;
  public static final Instant value3 = Instant.now();
  public static final Integer limit1 = 59;
  public static final int offset1 = 1001;

  @BeforeAll
  static void setUp() {
    if (DbDriverResolverContainer.has()) {
      return;
    }

    DbDriverResolverContainer.put(resolver);

    when(resolver.get(anyString())).thenReturn(dbDriver);
    when(dbDriver.getGrammar()).thenReturn(grammar);
    when(grammar.getQueryCollector()).thenReturn(pgsqlQueryCollector);
  }

  @AfterAll
  static void cleanUp() {
    DbDriverResolverContainer.put(null);
  }
}
