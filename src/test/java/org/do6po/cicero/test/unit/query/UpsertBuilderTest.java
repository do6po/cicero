package org.do6po.cicero.test.unit.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.do6po.cicero.query.PlainWriteBuilder;
import org.do6po.cicero.query.grammar.PostgresGrammar;
import org.junit.jupiter.api.Test;

class UpsertBuilderTest extends BaseQueryTest {

  private final PostgresGrammar postgresGrammar = new PostgresGrammar();

  @Test
  void upsert_single_record_with_update() {
    Map<String, Object> values = new LinkedHashMap<>();
    values.put("id", 1);
    values.put("username", "john");
    values.put("email", "john@example.com");

    var expression =
        postgresGrammar.compileUpsert(
            PlainWriteBuilder.query(table1),
            List.of(values),
            List.of("id"),
            List.of("username", "email"));

    assertEquals(
        "insert into %s (id, username, email) values (?, ?, ?) on conflict (id) do update set username = EXCLUDED.username, email = EXCLUDED.email"
            .formatted(table1),
        expression.getExpression());
    assertThat(expression.getBindings()).hasSize(3).containsExactly(1, "john", "john@example.com");
  }

  @Test
  void upsert_single_record_with_do_nothing() {
    Map<String, Object> values = new LinkedHashMap<>();
    values.put("id", 1);
    values.put("username", "john");
    values.put("email", "john@example.com");

    var expression =
        postgresGrammar.compileUpsert(
            PlainWriteBuilder.query(table1), List.of(values), List.of("id"), List.of());

    assertEquals(
        "insert into %s (id, username, email) values (?, ?, ?) on conflict (id) do nothing"
            .formatted(table1),
        expression.getExpression());
    assertThat(expression.getBindings()).hasSize(3).containsExactly(1, "john", "john@example.com");
  }

  @Test
  void upsert_single_record_with_null_update_columns() {
    Map<String, Object> values = new LinkedHashMap<>();
    values.put("id", 1);
    values.put("username", "john");

    var expression =
        postgresGrammar.compileUpsert(
            PlainWriteBuilder.query(table1), List.of(values), List.of("id"), null);

    assertEquals(
        "insert into %s (id, username) values (?, ?) on conflict (id) do nothing".formatted(table1),
        expression.getExpression());
  }

  @Test
  void upsert_multiple_records() {
    Map<String, Object> values1 = new LinkedHashMap<>();
    values1.put("id", 1);
    values1.put("username", "john");

    Map<String, Object> values2 = new LinkedHashMap<>();
    values2.put("id", 2);
    values2.put("username", "jane");

    List<Map<String, Object>> values = List.of(values1, values2);

    var expression =
        postgresGrammar.compileUpsert(
            PlainWriteBuilder.query(table1), values, List.of("id"), List.of("username"));

    assertEquals(
        "insert into %s (id, username) values (?, ?), (?, ?) on conflict (id) do update set username = EXCLUDED.username"
            .formatted(table1),
        expression.getExpression());
    assertThat(expression.getBindings()).hasSize(4).containsExactly(1, "john", 2, "jane");
  }

  @Test
  void upsert_with_multiple_conflict_columns() {
    Map<String, Object> values = new LinkedHashMap<>();
    values.put("email", "john@example.com");
    values.put("username", "john");
    values.put("status", "active");

    var expression =
        postgresGrammar.compileUpsert(
            PlainWriteBuilder.query(table1),
            List.of(values),
            List.of("email", "username"),
            List.of("status"));

    assertEquals(
        "insert into %s (email, username, status) values (?, ?, ?) on conflict (email, username) do update set status = EXCLUDED.status"
            .formatted(table1),
        expression.getExpression());
    assertThat(expression.getBindings())
        .hasSize(3)
        .containsExactly("john@example.com", "john", "active");
  }

  @Test
  void upsert_update_all_non_conflict_columns() {
    Map<String, Object> values = new LinkedHashMap<>();
    values.put("id", 1);
    values.put("username", "john");
    values.put("email", "john@example.com");
    values.put("status", "active");

    var expression =
        postgresGrammar.compileUpsert(
            PlainWriteBuilder.query(table1),
            List.of(values),
            List.of("id"),
            List.of("username", "email", "status"));

    assertEquals(
        "insert into %s (id, username, email, status) values (?, ?, ?, ?) on conflict (id) do update set username = EXCLUDED.username, email = EXCLUDED.email, status = EXCLUDED.status"
            .formatted(table1),
        expression.getExpression());
    assertThat(expression.getBindings())
        .hasSize(4)
        .containsExactly(1, "john", "john@example.com", "active");
  }

  @Test
  void upsert_with_single_update_column() {
    Map<String, Object> values = new LinkedHashMap<>();
    values.put("id", 1);
    values.put("username", "john");
    values.put("counter", 5);

    var expression =
        postgresGrammar.compileUpsert(
            PlainWriteBuilder.query(table1), List.of(values), List.of("id"), List.of("counter"));

    assertEquals(
        "insert into %s (id, username, counter) values (?, ?, ?) on conflict (id) do update set counter = EXCLUDED.counter"
            .formatted(table1),
        expression.getExpression());
  }

  @Test
  void upsert_batch_with_do_nothing() {
    Map<String, Object> values1 = new LinkedHashMap<>();
    values1.put("id", 1);
    values1.put("username", "john");

    Map<String, Object> values2 = new LinkedHashMap<>();
    values2.put("id", 2);
    values2.put("username", "jane");

    Map<String, Object> values3 = new LinkedHashMap<>();
    values3.put("id", 3);
    values3.put("username", "bob");

    List<Map<String, Object>> values = List.of(values1, values2, values3);

    var expression =
        postgresGrammar.compileUpsert(
            PlainWriteBuilder.query(table1), values, List.of("id"), List.of());

    assertEquals(
        "insert into %s (id, username) values (?, ?), (?, ?), (?, ?) on conflict (id) do nothing"
            .formatted(table1),
        expression.getExpression());
    assertThat(expression.getBindings()).hasSize(6).containsExactly(1, "john", 2, "jane", 3, "bob");
  }
}
