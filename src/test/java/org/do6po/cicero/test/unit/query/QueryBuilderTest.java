package org.do6po.cicero.test.unit.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.do6po.cicero.query.QueryBuilder.query;
import static org.do6po.cicero.utils.DotUtil.dot;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import org.do6po.cicero.expression.SqlExpression;
import org.do6po.cicero.expression.join.JoinExpression.JoinTypeEnum;
import org.junit.jupiter.api.Test;

class QueryBuilderTest {

  String table1 = "table1";
  String table2 = "table2";
  String table3 = "table3";

  String column1 = "column1";
  String column2 = "column2";
  String column3 = "column3";

  String value1 = "Some text";
  Integer value2 = 100;
  Instant value3 = Instant.now();

  @Test
  void from() {
    SqlExpression expression = query(table1).getSqlExpression();

    assertEquals("SELECT * FROM %s".formatted(table1), expression.getExpression());
  }

  @Test
  void select_one_column__from() {
    SqlExpression expression = query(table1).select(column1).getSqlExpression();

    assertEquals("SELECT %s FROM %s".formatted(column1, table1), expression.getExpression());
  }

  @Test
  void select_two_column__from() {
    SqlExpression expression = query(table1).select(column1, column2).getSqlExpression();

    assertEquals("SELECT %s, %s FROM %s".formatted(column1, column2, table1),
        expression.getExpression());
  }

  @Test
  void from__inner_join_method1() {
    String sql = query(table1).join(table2, dot(table2, column1), "=", dot(table1, column2),
        JoinTypeEnum.INNER_JOIN).getSqlExpression().getExpression();

    assertEquals(
        "SELECT * FROM %s INNER JOIN %s ON %s.%s = %s.%s".formatted(table1, table2, table2, column1,
            table1, column2), sql);
  }

  @Test
  void from__inner_join_and_criteria() {
    SqlExpression expression = query(table1).join(
        j -> j.table(table2).on(dot(table2, column1), "=", dot(table1, column2))
            .where(dot(table3, column1), value1)).getSqlExpression();

    assertEquals(
        "SELECT * FROM %s INNER JOIN %s ON %s.%s = %s.%s AND %s.%s = (?)".formatted(table1, table2,
            table2, column1, table1, column2, table3, column1), expression.getExpression());

    assertThat(expression.getBindings()).hasSize(1).containsExactly(value1);
  }

  @Test
  void from__left_join() {
    String sql = query(table1).join(table2, dot(table2, column1), "=", dot(table1, column2),
        JoinTypeEnum.LEFT_OUTER).getSqlExpression().getExpression();

    assertEquals(
        "SELECT * FROM %s LEFT OUTER JOIN %s ON %s.%s = %s.%s".formatted(table1, table2, table2,
            column1,
            table1, column2), sql);
  }

  @Test
  void from__right_join() {
    String sql = query(table1).join(table2, dot(table2, column1), "=", dot(table1, column2),
        JoinTypeEnum.RIGHT_OUTER).getSqlExpression().getExpression();

    assertEquals(
        "SELECT * FROM %s RIGHT OUTER JOIN %s ON %s.%s = %s.%s".formatted(table1, table2, table2,
            column1,
            table1, column2), sql);
  }

  @Test
  void from__where_one_criteria() {
    SqlExpression expression = query(table1).where(dot(table1, column2), "=", value1)
        .getSqlExpression();

    assertEquals(
        "SELECT * FROM %s WHERE %s.%s = (?)".formatted(
            table1, table1, column2), expression.getExpression());
  }

  @Test
  void from__where_criteria1_and_criteria2() {
    SqlExpression expression = query(table1).where(column2, "=", value1)
        .where(column1, value2)
        .getSqlExpression();

    assertEquals(
        "SELECT * FROM %s WHERE %s = (?) AND %s = (?)".formatted(
            table1, column2, column1), expression.getExpression());
  }

  @Test
  void from__where_criteria1_or_criteria2() {
    SqlExpression expression = query(table1).where(column2, "=", value1)
        .orWhere(column1, value2)
        .getSqlExpression();

    assertEquals(
        "SELECT * FROM %s WHERE %s = (?) OR %s = (?)".formatted(
            table1, column2, column1), expression.getExpression());

    assertThat(expression.getBindings())
        .hasSize(2)
        .containsExactly(value1, value2);
  }

  @Test
  void from__where_criteria1_and_nested_criteria2_or_criteria3() {

    SqlExpression expression = query(table1).where(column2, "=", value1)
        .where(q -> q.where(column1, value2)
            .orWhere(column3, value3))
        .getSqlExpression();

    assertEquals(
        "SELECT * FROM %s WHERE %s = (?) AND (%s = (?) OR %s = (?))".formatted(
            table1, column2, column1, column3), expression.getExpression());

    assertThat(expression.getBindings())
        .hasSize(3)
        .containsExactly(value1, value2, value3);
  }
}
