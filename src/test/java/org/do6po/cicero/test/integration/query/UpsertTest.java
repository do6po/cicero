package org.do6po.cicero.test.integration.query;

import static java.util.UUID.randomUUID;
import static org.do6po.cicero.query.AttributeHolder.attrs;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.darrmirr.dbchange.annotation.SqlExecutorGetter;
import com.github.darrmirr.dbchange.annotation.onclass.DbChangeOnce;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.do6po.cicero.query.PlainWriteBuilder;
import org.do6po.cicero.test.integration.BaseDbTest;
import org.junit.jupiter.api.Test;

@SqlExecutorGetter(BaseDbTest.DEFAULT_SQL_EXECUTOR)
@DbChangeOnce(sqlQueryFiles = "sql/users__up.sql")
@DbChangeOnce(
    sqlQueryFiles = "sql/users__down.sql",
    executionPhase = DbChangeOnce.ExecutionPhase.AFTER_ALL)
class UpsertTest extends BaseDbTest {

  @Test
  void upsert_insert_new_record() {
    String id = randomUUID().toString();

    LinkedHashMap<String, Object> attrs =
        attrs()
            .put("id", id)
            .put("username", "newuser")
            .put("password", "pass123")
            .put("created_at", Timestamp.from(Instant.now()))
            .put("updated_at", Timestamp.from(Instant.now()))
            .getAttributes();

    assertTrue(userQuery().whereKey(id).doesNotExists());

    startQueryCount();

    int affected =
        PlainWriteBuilder.query("users")
            .upsert(attrs, List.of("id"), List.of("username", "password"));

    assertQueryCount(1);
    assertEquals(1, affected);
    assertTrue(userQuery().whereKey(id).exists());
    assertEquals(
        "newuser", userQuery().whereKey(id).first().orElseThrow().getAttribute("username"));
  }

  @Test
  void upsert_update_existing_record() {
    String id = "4fbcfc50-7dda-4c1c-b358-30e70cb8b6d8"; // existing user1

    String oldUsername = userQuery().whereKey(id).first().orElseThrow().getAttribute("username");

    LinkedHashMap<String, Object> attrs =
        attrs()
            .put("id", id)
            .put("username", "updated_user")
            .put("password", "newpassword")
            .put("created_at", Timestamp.from(Instant.now()))
            .put("updated_at", Timestamp.from(Instant.now()))
            .getAttributes();

    startQueryCount();

    int affected =
        PlainWriteBuilder.query("users")
            .upsert(attrs, List.of("id"), List.of("username", "password"));

    assertQueryCount(1);
    assertEquals(1, affected);

    String newUsername = userQuery().whereKey(id).first().orElseThrow().getAttribute("username");
    assertEquals("updated_user", newUsername);
    assertTrue(!oldUsername.equals(newUsername));
  }

  @Test
  void upsert_batch_mixed_insert_and_update() {
    String existingId = "4fbcfc50-7dda-4c1c-b358-30e70cb8b6d8"; // existing
    String newId = randomUUID().toString(); // new

    List<Map<String, Object>> records =
        List.of(
            attrs()
                .put("id", existingId)
                .put("username", "updated_existing")
                .put("password", "pass1")
                .put("created_at", Timestamp.from(Instant.now()))
                .put("updated_at", Timestamp.from(Instant.now()))
                .getAttributes(),
            attrs()
                .put("id", newId)
                .put("username", "brand_new")
                .put("password", "pass2")
                .put("created_at", Timestamp.from(Instant.now()))
                .put("updated_at", Timestamp.from(Instant.now()))
                .getAttributes());

    assertTrue(userQuery().whereKey(existingId).exists());
    assertTrue(userQuery().whereKey(newId).doesNotExists());

    startQueryCount();

    int affected =
        PlainWriteBuilder.query("users")
            .upsert(records, List.of("id"), List.of("username", "password"));

    assertQueryCount(1);
    assertEquals(2, affected);

    assertEquals(
        "updated_existing",
        userQuery().whereKey(existingId).first().orElseThrow().getAttribute("username"));
    assertEquals(
        "brand_new", userQuery().whereKey(newId).first().orElseThrow().getAttribute("username"));
  }

  @Test
  void upsert_with_do_nothing() {
    String id = "4fbcfc50-7dda-4c1c-b358-30e70cb8b6d8"; // existing

    String oldUsername = userQuery().whereKey(id).first().orElseThrow().getAttribute("username");

    LinkedHashMap<String, Object> attrs =
        attrs()
            .put("id", id)
            .put("username", "should_not_update")
            .put("created_at", Timestamp.from(Instant.now()))
            .put("updated_at", Timestamp.from(Instant.now()))
            .getAttributes();

    startQueryCount();

    int affected = PlainWriteBuilder.query("users").upsert(attrs, List.of("id"), List.of());

    assertQueryCount(1);
    assertEquals(0, affected);

    String unchangedUsername =
        userQuery().whereKey(id).first().orElseThrow().getAttribute("username");
    assertEquals(oldUsername, unchangedUsername);
  }

  @Test
  void upsert_partial_update() {
    String id = "4fbcfc50-7dda-4c1c-b358-30e70cb8b6d8"; // existing

    String oldPassword = userQuery().whereKey(id).first().orElseThrow().getAttribute("password");

    LinkedHashMap<String, Object> attrs =
        attrs()
            .put("id", id)
            .put("username", "only_username_updated")
            .put("password", "should_not_change")
            .put("created_at", Timestamp.from(Instant.now()))
            .put("updated_at", Timestamp.from(Instant.now()))
            .getAttributes();

    startQueryCount();

    int affected =
        PlainWriteBuilder.query("users").upsert(attrs, List.of("id"), List.of("username"));

    assertQueryCount(1);
    assertEquals(1, affected);

    String newUsername = userQuery().whereKey(id).first().orElseThrow().getAttribute("username");
    String unchangedPassword =
        userQuery().whereKey(id).first().orElseThrow().getAttribute("password");

    assertEquals("only_username_updated", newUsername);
    assertEquals(oldPassword, unchangedPassword);
  }

  @Test
  void upsert_model_query_builder_with_primary_key() {
    String id = randomUUID().toString();

    LinkedHashMap<String, Object> attrs =
        attrs()
            .put("id", id)
            .put("username", "model_upsert_user")
            .put("created_at", Timestamp.from(Instant.now()))
            .put("updated_at", Timestamp.from(Instant.now()))
            .getAttributes();

    assertTrue(userQuery().whereKey(id).doesNotExists());

    startQueryCount();

    int affected = userQuery().upsert(attrs, List.of("username"));

    assertQueryCount(1);
    assertEquals(1, affected);
    assertTrue(userQuery().whereKey(id).exists());
    assertEquals(
        "model_upsert_user",
        userQuery().whereKey(id).first().orElseThrow().getAttribute("username"));
  }

  @Test
  void upsert_model_update_existing() {
    String id = "4fbcfc50-7dda-4c1c-b358-30e70cb8b6d8"; // existing

    LinkedHashMap<String, Object> attrs =
        attrs()
            .put("id", id)
            .put("username", "model_updated")
            .put("created_at", Timestamp.from(Instant.now()))
            .put("updated_at", Timestamp.from(Instant.now()))
            .getAttributes();

    startQueryCount();

    int affected = userQuery().upsert(attrs, List.of("username"));

    assertQueryCount(1);
    assertEquals(1, affected);
    assertEquals(
        "model_updated", userQuery().whereKey(id).first().orElseThrow().getAttribute("username"));
  }
}
