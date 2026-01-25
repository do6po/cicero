package org.do6po.cicero.test.unit.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.do6po.cicero.query.PlainWriteBuilder;
import org.do6po.cicero.query.grammar.PostgresGrammar;
import org.junit.jupiter.api.Test;

class DeleteBuilderTest extends BaseQueryTest {

  private final PostgresGrammar postgresGrammar = new PostgresGrammar();

  @Test
  void delete_without_where() {
    var expression = postgresGrammar.compileDelete(PlainWriteBuilder.query(table1));

    assertEquals("delete from %s".formatted(table1), expression.getExpression());
    assertThat(expression.getBindings()).isEmpty();
  }

  @Test
  void delete_with_single_where() {
    var expression =
        postgresGrammar.compileDelete(PlainWriteBuilder.query(table1).where(column1, value1));

    assertEquals(
        "delete from %s where %s = (?)".formatted(table1, column1), expression.getExpression());
    assertThat(expression.getBindings()).hasSize(1).containsExactly(value1);
  }

  @Test
  void delete_with_multiple_where() {
    var expression =
        postgresGrammar.compileDelete(
            PlainWriteBuilder.query(table1).where(column1, value1).where(column2, value2));

    assertEquals(
        "delete from %s where %s = (?) AND %s = (?)".formatted(table1, column1, column2),
        expression.getExpression());
    assertThat(expression.getBindings()).hasSize(2).containsExactly(value1, value2);
  }

  @Test
  void delete_with_or_where() {
    var expression =
        postgresGrammar.compileDelete(
            PlainWriteBuilder.query(table1).where(column1, value1).orWhere(column2, value2));

    assertEquals(
        "delete from %s where %s = (?) OR %s = (?)".formatted(table1, column1, column2),
        expression.getExpression());
    assertThat(expression.getBindings()).hasSize(2).containsExactly(value1, value2);
  }

  @Test
  void delete_with_where_in() {
    var expression =
        postgresGrammar.compileDelete(
            PlainWriteBuilder.query(table1).whereIn(column1, java.util.List.of(1, 2, 3)));

    assertEquals(
        "delete from %s where %s IN (?, ?, ?)".formatted(table1, column1),
        expression.getExpression());
    assertThat(expression.getBindings()).hasSize(3).containsExactly(1, 2, 3);
  }

  @Test
  void delete_with_is_null() {
    var expression = postgresGrammar.compileDelete(PlainWriteBuilder.query(table1).isNull(column1));

    assertEquals(
        "delete from %s where %s IS NULL".formatted(table1, column1), expression.getExpression());
    assertThat(expression.getBindings()).isEmpty();
  }

  @Test
  void delete_with_is_not_null() {
    var expression =
        postgresGrammar.compileDelete(PlainWriteBuilder.query(table1).isNotNull(column1));

    assertEquals(
        "delete from %s where %s IS NOT NULL".formatted(table1, column1),
        expression.getExpression());
    assertThat(expression.getBindings()).isEmpty();
  }

  @Test
  void delete_with_nested_where() {
    var expression =
        postgresGrammar.compileDelete(
            PlainWriteBuilder.query(table1)
                .where(q -> q.where(column1, value1).orWhere(column2, value2)));

    assertEquals(
        "delete from %s where (%s = (?) OR %s = (?))".formatted(table1, column1, column2),
        expression.getExpression());
    assertThat(expression.getBindings()).hasSize(2).containsExactly(value1, value2);
  }

  @Test
  void delete_with_comparison_operator() {
    var expression =
        postgresGrammar.compileDelete(PlainWriteBuilder.query(table1).where(column1, ">", value2));

    assertEquals(
        "delete from %s where %s > (?)".formatted(table1, column1), expression.getExpression());
    assertThat(expression.getBindings()).hasSize(1).containsExactly(value2);
  }

  @Test
  void delete_with_raw_where() {
    String rawCondition = "custom_function(%s)".formatted(column1);
    var expression =
        postgresGrammar.compileDelete(
            PlainWriteBuilder.query(table1)
                .whereRaw(rawCondition + " = ?", java.util.List.of(value1)));

    assertEquals(
        "delete from %s where %s = ?".formatted(table1, rawCondition), expression.getExpression());
    assertThat(expression.getBindings()).hasSize(1).containsExactly(value1);
  }

  @Test
  void delete_with_complex_conditions() {
    var expression =
        postgresGrammar.compileDelete(
            PlainWriteBuilder.query(table1)
                .where(column1, value1)
                .where(q -> q.where(column2, ">", value2).orWhere(w -> w.isNull(column3))));

    assertEquals(
        "delete from %s where %s = (?) AND (%s > (?) OR (%s IS NULL))"
            .formatted(table1, column1, column2, column3),
        expression.getExpression());
    assertThat(expression.getBindings()).hasSize(2).containsExactly(value1, value2);
  }
}
