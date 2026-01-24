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
import org.do6po.cicero.query.PlainWriteBuilder;
import org.do6po.cicero.test.integration.BaseDbTest;
import org.junit.jupiter.api.Test;

@SqlExecutorGetter(BaseDbTest.DEFAULT_SQL_EXECUTOR)
@DbChangeOnce(sqlQueryFiles = "sql/users__up.sql")
@DbChangeOnce(
    sqlQueryFiles = "sql/users__down.sql",
    executionPhase = DbChangeOnce.ExecutionPhase.AFTER_ALL)
class PlainWriteBuilderTest extends BaseDbTest {

  @Test
  void insert() {
    String id = randomUUID().toString();

    LinkedHashMap<String, Object> attrs =
        attrs()
            .put("id", id)
            .put("username", "User Name")
            .put("created_at", Timestamp.from(Instant.now()))
            .put("updated_at", Timestamp.from(Instant.now()))
            .getAttributes();

    assertTrue(userQuery().whereAre(attrs).doesNotExists());

    startQueryCount();

    int inserted = PlainWriteBuilder.query("users").insert(attrs);

    assertQueryCount(1);
    assertEquals(1, inserted);

    assertTrue(userQuery().whereAre(attrs).exists());
  }

  @Test
  void insertMany() {
    String id1 = randomUUID().toString();
    String id2 = randomUUID().toString();

    LinkedHashMap<String, Object> attrs1 =
        attrs()
            .put("id", id1)
            .put("username", "User Name 1")
            .put("created_at", Timestamp.from(Instant.now()))
            .put("updated_at", Timestamp.from(Instant.now()))
            .getAttributes();

    LinkedHashMap<String, Object> attrs2 =
        attrs()
            .put("id", id2)
            .put("username", "User Name 2")
            .put("created_at", Timestamp.from(Instant.now()))
            .put("updated_at", Timestamp.from(Instant.now()))
            .getAttributes();

    assertTrue(userQuery().whereAre(attrs1).doesNotExists());
    assertTrue(userQuery().whereAre(attrs2).doesNotExists());

    startQueryCount();

    int inserted = PlainWriteBuilder.query("users").insert(List.of(attrs1, attrs2));

    assertQueryCount(1);
    assertEquals(2, inserted);

    assertTrue(userQuery().whereAre(attrs1).exists());
    assertTrue(userQuery().whereAre(attrs2).exists());
  }

  @Test
  void update_where() {
    String id = "4fbcfc50-7dda-4c1c-b358-30e70cb8b6d8";

    LinkedHashMap<String, Object> attrs =
        attrs().put("id", id).put("username", "user1").put("password", "123456").getAttributes();

    LinkedHashMap<String, Object> toUpdate =
        attrs().put("username", "user1 changed").put("password", "123456 chaneged").getAttributes();

    assertTrue(userQuery().whereAre(attrs).exists());
    assertTrue(userQuery().whereAre(toUpdate).doesNotExists());

    startQueryCount();

    int updated =
        PlainWriteBuilder.query("users")
            .where("id", id)
            .where("username", "user1")
            .update(toUpdate);

    assertQueryCount(1);
    assertEquals(1, updated);

    assertTrue(userQuery().whereAre(attrs).doesNotExists());
    assertTrue(userQuery().whereAre(toUpdate).exists());
  }

  @Test
  void update_all() {
    LinkedHashMap<String, Object> toUpdate =
        attrs().put("updated_at", Timestamp.from(Instant.now())).getAttributes();

    startQueryCount();

    int updated = PlainWriteBuilder.query("users").update(toUpdate);

    assertQueryCount(1);
    assertTrue(updated > 1);

    assertTrue(userQuery().whereAre(toUpdate).exists());
  }

  @Test
  void delete_single_record() {
    String id = randomUUID().toString();

    LinkedHashMap<String, Object> attrs =
        attrs()
            .put("id", id)
            .put("username", "User to Delete")
            .put("created_at", Timestamp.from(Instant.now()))
            .put("updated_at", Timestamp.from(Instant.now()))
            .getAttributes();

    // Insert the record first
    PlainWriteBuilder.query("users").insert(attrs);
    assertTrue(userQuery().whereAre(attrs).exists());

    startQueryCount();

    // Delete the record
    int deleted = PlainWriteBuilder.query("users").where("id", id).delete();

    assertQueryCount(1);
    assertEquals(1, deleted);

    // Verify record is gone
    assertTrue(userQuery().where("id", id).doesNotExists());
  }

  @Test
  void delete_multiple_records_with_where_in() {
    String id1 = randomUUID().toString();
    String id2 = randomUUID().toString();
    String id3 = randomUUID().toString();

    // Insert three records
    PlainWriteBuilder.query("users")
        .insert(
            List.of(
                attrs()
                    .put("id", id1)
                    .put("username", "User 1")
                    .put("created_at", Timestamp.from(Instant.now()))
                    .put("updated_at", Timestamp.from(Instant.now()))
                    .getAttributes(),
                attrs()
                    .put("id", id2)
                    .put("username", "User 2")
                    .put("created_at", Timestamp.from(Instant.now()))
                    .put("updated_at", Timestamp.from(Instant.now()))
                    .getAttributes(),
                attrs()
                    .put("id", id3)
                    .put("username", "User 3")
                    .put("created_at", Timestamp.from(Instant.now()))
                    .put("updated_at", Timestamp.from(Instant.now()))
                    .getAttributes()));

    // Verify all three exist
    assertTrue(userQuery().where("id", id1).exists());
    assertTrue(userQuery().where("id", id2).exists());
    assertTrue(userQuery().where("id", id3).exists());

    startQueryCount();

    // Delete two of them using WHERE IN
    int deleted =
        PlainWriteBuilder.query("users")
            .whereIn("id", java.util.List.of(id1, (Object) id2))
            .delete();

    assertQueryCount(1);
    assertEquals(2, deleted);

    // Verify two are gone, one remains
    assertTrue(userQuery().where("id", id1).doesNotExists());
    assertTrue(userQuery().where("id", id2).doesNotExists());
    assertTrue(userQuery().where("id", id3).exists());
  }

  @Test
  void delete_with_complex_where() {
    String id1 = randomUUID().toString();
    String id2 = randomUUID().toString();
    String username = "ComplexDeleteUser";

    // Insert two records with same username but different IDs
    PlainWriteBuilder.query("users")
        .insert(
            List.of(
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

    // Delete only the one with password1
    int deleted =
        PlainWriteBuilder.query("users")
            .where("username", username)
            .where("password", "password1")
            .delete();

    assertQueryCount(1);
    assertEquals(1, deleted);

    // Verify only one is deleted
    assertTrue(userQuery().where("id", id1).doesNotExists());
    assertTrue(userQuery().where("id", id2).exists());
  }

  @Test
  void delete_with_is_null() {
    String id = randomUUID().toString();

    LinkedHashMap<String, Object> attrs =
        attrs()
            .put("id", id)
            .put("username", "UserWithNullPassword")
            .put("created_at", Timestamp.from(Instant.now()))
            .put("updated_at", Timestamp.from(Instant.now()))
            .getAttributes();

    PlainWriteBuilder.query("users").insert(attrs);
    assertTrue(userQuery().where("id", id).exists());

    startQueryCount();

    // Delete where password is NULL
    int deleted = PlainWriteBuilder.query("users").where("id", id).isNull("password").delete();

    assertQueryCount(1);
    assertEquals(1, deleted);
    assertTrue(userQuery().where("id", id).doesNotExists());
  }

  @Test
  void delete_no_where_deletes_nothing_when_no_match() {
    // Get initial count
    long initialCount = userQuery().getCountForPagination();

    startQueryCount();

    // Delete with impossible condition
    int deleted = PlainWriteBuilder.query("users").where("id", "impossible-id-999").delete();

    assertQueryCount(1);
    assertEquals(0, deleted);

    // Verify count hasn't changed
    assertEquals(initialCount, userQuery().getCountForPagination());
  }
}
