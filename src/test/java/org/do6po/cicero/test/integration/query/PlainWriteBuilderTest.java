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
}
