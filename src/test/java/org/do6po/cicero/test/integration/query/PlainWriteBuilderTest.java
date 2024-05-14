package org.do6po.cicero.test.integration.query;

import static java.util.UUID.randomUUID;
import static org.do6po.cicero.query.AttributeHolder.attrs;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.darrmirr.dbchange.annotation.SqlExecutorGetter;
import com.github.darrmirr.dbchange.annotation.onclass.DbChangeOnce;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.LinkedHashMap;
import org.do6po.cicero.query.PlainWriteBuilder;
import org.do6po.cicero.test.integration.BaseDbTest;
import org.junit.jupiter.api.Test;

@SqlExecutorGetter("defaultSqlExecutor")
@DbChangeOnce(sqlQueryFiles = "sql/users__up.sql")
@DbChangeOnce(
    sqlQueryFiles = "sql/users__down.sql",
    executionPhase = DbChangeOnce.ExecutionPhase.AFTER_ALL)
public class PlainWriteBuilderTest extends BaseDbTest {

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

    PlainWriteBuilder.query("users").insert(attrs);

    assertQueryCount(1);

    assertTrue(userQuery().whereAre(attrs).exists());
  }
}
