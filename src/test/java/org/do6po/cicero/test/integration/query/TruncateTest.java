package org.do6po.cicero.test.integration.query;

import static com.github.darrmirr.dbchange.annotation.onclass.DbChangeOnce.ExecutionPhase.AFTER_ALL;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.darrmirr.dbchange.annotation.SqlExecutorGetter;
import com.github.darrmirr.dbchange.annotation.onclass.DbChangeOnce;
import org.do6po.cicero.query.PlainWriteBuilder;
import org.do6po.cicero.test.integration.BaseDbTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@SqlExecutorGetter(BaseDbTest.DEFAULT_SQL_EXECUTOR)
@DbChangeOnce(sqlQueryFiles = "sql/users__up.sql")
@DbChangeOnce(sqlQueryFiles = "sql/users__down.sql", executionPhase = AFTER_ALL)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TruncateTest extends BaseDbTest {

  @Test
  @Order(1)
  void truncate_removes_all_records() {
    assertEquals(6, userQuery().get().size());

    PlainWriteBuilder.query("users").truncate();

    assertEquals(0, userQuery().get().size());
  }

  @Test
  @Order(2)
  void truncate_on_empty_table() {
    PlainWriteBuilder.query("users").truncate();
    assertEquals(0, userQuery().get().size());

    PlainWriteBuilder.query("users").truncate();
    assertEquals(0, userQuery().get().size());
  }

  @Test
  @Order(3)
  void truncate_with_model_query_builder() {
    userQuery().truncate();

    assertEquals(0, userQuery().get().size());
  }
}
