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
import org.do6po.cicero.query.PlainWriteBuilder;
import org.do6po.cicero.test.integration.BaseDbTest;
import org.junit.jupiter.api.Test;

@SqlExecutorGetter(BaseDbTest.DEFAULT_SQL_EXECUTOR)
@DbChangeOnce(sqlQueryFiles = "sql/users__up.sql")
@DbChangeOnce(
    sqlQueryFiles = "sql/users__down.sql",
    executionPhase = DbChangeOnce.ExecutionPhase.AFTER_ALL)
class ModelDeleteTest extends BaseDbTest {

  @Test
  void delete_by_primary_key() {
    String id = randomUUID().toString();

    LinkedHashMap<String, Object> attrs =
        attrs()
            .put("id", id)
            .put("username", "DeleteByKey")
            .put("created_at", Timestamp.from(Instant.now()))
            .put("updated_at", Timestamp.from(Instant.now()))
            .getAttributes();

    // Insert record
    PlainWriteBuilder.query("users").insert(attrs);
    assertTrue(userQuery().whereKey(id).exists());

    startQueryCount();

    // Delete using whereKey
    int deleted = userQuery().whereKey(id).delete();

    assertQueryCount(1);
    assertEquals(1, deleted);
    assertTrue(userQuery().whereKey(id).doesNotExists());
  }

  @Test
  void delete_by_multiple_primary_keys() {
    String id1 = randomUUID().toString();
    String id2 = randomUUID().toString();
    String id3 = randomUUID().toString();

    // Insert records
    PlainWriteBuilder.query("users")
        .insert(
            java.util.List.of(
                attrs()
                    .put("id", id1)
                    .put("username", "User1")
                    .put("created_at", Timestamp.from(Instant.now()))
                    .put("updated_at", Timestamp.from(Instant.now()))
                    .getAttributes(),
                attrs()
                    .put("id", id2)
                    .put("username", "User2")
                    .put("created_at", Timestamp.from(Instant.now()))
                    .put("updated_at", Timestamp.from(Instant.now()))
                    .getAttributes(),
                attrs()
                    .put("id", id3)
                    .put("username", "User3")
                    .put("created_at", Timestamp.from(Instant.now()))
                    .put("updated_at", Timestamp.from(Instant.now()))
                    .getAttributes()));

    assertTrue(userQuery().whereKey(id1).exists());
    assertTrue(userQuery().whereKey(id2).exists());
    assertTrue(userQuery().whereKey(id3).exists());

    startQueryCount();

    // Delete two of them
    int deleted = userQuery().whereKey(java.util.List.of(id1, id2)).delete();

    assertQueryCount(1);
    assertEquals(2, deleted);

    assertTrue(userQuery().whereKey(id1).doesNotExists());
    assertTrue(userQuery().whereKey(id2).doesNotExists());
    assertTrue(userQuery().whereKey(id3).exists());
  }

  @Test
  void delete_with_where_conditions() {
    String id1 = randomUUID().toString();
    String id2 = randomUUID().toString();
    String username = "TestUser";

    // Insert records
    PlainWriteBuilder.query("users")
        .insert(
            java.util.List.of(
                attrs()
                    .put("id", id1)
                    .put("username", username)
                    .put("password", "password1")
                    .put("created_at", Timestamp.from(Instant.now()))
                    .put("updated_at", Timestamp.from(Instant.now()))
                    .getAttributes(),
                attrs()
                    .put("id", id2)
                    .put("username", username)
                    .put("password", "password2")
                    .put("created_at", Timestamp.from(Instant.now()))
                    .put("updated_at", Timestamp.from(Instant.now()))
                    .getAttributes()));

    assertTrue(userQuery().where("id", id1).exists());
    assertTrue(userQuery().where("id", id2).exists());

    startQueryCount();

    // Delete specific record
    int deleted = userQuery().where("username", username).where("password", "password1").delete();

    assertQueryCount(1);
    assertEquals(1, deleted);

    assertTrue(userQuery().where("id", id1).doesNotExists());
    assertTrue(userQuery().where("id", id2).exists());
  }

  @Test
  void delete_no_matches() {
    startQueryCount();

    int deleted = userQuery().whereKey("non-existent-id-12345").delete();

    assertQueryCount(1);
    assertEquals(0, deleted);
  }

  @Test
  void delete_with_nested_conditions() {
    String id1 = randomUUID().toString();
    String id2 = randomUUID().toString();
    String id3 = randomUUID().toString();

    // Insert records
    PlainWriteBuilder.query("users")
        .insert(
            java.util.List.of(
                attrs()
                    .put("id", id1)
                    .put("username", "user_active")
                    .put("password", "pass1")
                    .put("created_at", Timestamp.from(Instant.now()))
                    .put("updated_at", Timestamp.from(Instant.now()))
                    .getAttributes(),
                attrs()
                    .put("id", id2)
                    .put("username", "user_inactive")
                    .put("password", "pass2")
                    .put("created_at", Timestamp.from(Instant.now()))
                    .put("updated_at", Timestamp.from(Instant.now()))
                    .getAttributes(),
                attrs()
                    .put("id", id3)
                    .put("username", "admin_active")
                    .put("password", "pass3")
                    .put("created_at", Timestamp.from(Instant.now()))
                    .put("updated_at", Timestamp.from(Instant.now()))
                    .getAttributes()));

    assertTrue(userQuery().whereKey(id1).exists());
    assertTrue(userQuery().whereKey(id2).exists());
    assertTrue(userQuery().whereKey(id3).exists());

    startQueryCount();

    // Delete with nested OR condition
    int deleted =
        userQuery()
            .where(q -> q.where("username", "user_active").orWhere("username", "user_inactive"))
            .delete();

    assertQueryCount(1);
    assertEquals(2, deleted);

    assertTrue(userQuery().whereKey(id1).doesNotExists());
    assertTrue(userQuery().whereKey(id2).doesNotExists());
    assertTrue(userQuery().whereKey(id3).exists());
  }
}
