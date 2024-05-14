package org.do6po.cicero.test.integration.query;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.darrmirr.dbchange.annotation.SqlExecutorGetter;
import com.github.darrmirr.dbchange.annotation.onclass.DbChangeOnce;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;
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

    Map<String, Object> values =
        Map.of(
            "id",
            id,
            "username",
            "User Name",
            "created_at",
            Timestamp.from(Instant.now()),
            "updated_at",
            Timestamp.from(Instant.now()));

    assertTrue(userQuery().whereKey(id).doesNotExists());

    PlainWriteBuilder.query("users").insert(values);

    assertTrue(userQuery().whereKey(id).exists());
  }
}
