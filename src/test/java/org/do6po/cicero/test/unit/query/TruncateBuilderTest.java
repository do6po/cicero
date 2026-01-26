package org.do6po.cicero.test.unit.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.do6po.cicero.expression.write.PostgresTruncateExpression;
import org.do6po.cicero.query.PlainWriteBuilder;
import org.do6po.cicero.query.grammar.PostgresGrammar;
import org.junit.jupiter.api.Test;

class TruncateBuilderTest extends BaseQueryTest {

  private final PostgresGrammar postgresGrammar = new PostgresGrammar();

  @Test
  void truncate_simple_table() {
    PostgresTruncateExpression expression =
        (PostgresTruncateExpression)
            postgresGrammar.compileTruncate(PlainWriteBuilder.query(table1));

    assertEquals("TRUNCATE TABLE %s".formatted(table1), expression.getExpression());
    assertThat(expression.getBindings()).isEmpty();
  }

  @Test
  void truncate_different_table() {
    PostgresTruncateExpression expression =
        (PostgresTruncateExpression)
            postgresGrammar.compileTruncate(PlainWriteBuilder.query(table2));

    assertEquals("TRUNCATE TABLE %s".formatted(table2), expression.getExpression());
    assertThat(expression.getBindings()).isEmpty();
  }

  @Test
  void truncate_expression_has_no_bindings() {
    PostgresTruncateExpression expression =
        (PostgresTruncateExpression)
            postgresGrammar.compileTruncate(PlainWriteBuilder.query(table3));

    assertThat(expression.getBindings()).isEmpty();
  }
}
