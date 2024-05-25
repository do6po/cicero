package org.do6po.cicero.test.integration.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.darrmirr.dbchange.annotation.SqlExecutorGetter;
import com.github.darrmirr.dbchange.annotation.onclass.DbChangeOnce;
import com.github.darrmirr.dbchange.annotation.onclass.DbChangeOnce.ExecutionPhase;
import java.util.List;
import java.util.Set;
import org.do6po.cicero.pagination.Paginator;
import org.do6po.cicero.test.integration.BaseDbTest;
import org.do6po.cicero.test.integration.model.UserM;
import org.junit.jupiter.api.Test;

@SqlExecutorGetter(BaseDbTest.DEFAULT_SQL_EXECUTOR)
@DbChangeOnce(sqlQueryFiles = "sql/users__up.sql")
@DbChangeOnce(sqlQueryFiles = "sql/users__down.sql", executionPhase = ExecutionPhase.AFTER_ALL)
class ModelQueryBuilderTest extends BaseDbTest {

  @Test
  void findById() {
    UserM user = userQuery().find(USER1_ID).orElseThrow();

    assertThat(user).isNotNull();

    assertEquals(USER1_ID, user.getId());
  }

  @Test
  void findByIds() {
    List<UserM> users = userQuery().find(Set.of(USER1_ID, USER2_ID));

    assertThat(users).hasSize(2).map(UserM::getId).contains(USER1_ID, USER2_ID);
  }

  @Test
  void whereKey() {
    List<UserM> users = userQuery().whereKey(Set.of(USER1_ID, USER2_ID, USER3_ID)).get();

    assertThat(users).hasSize(3).map(UserM::getId).contains(USER1_ID, USER2_ID, USER3_ID);
  }

  @Test
  void paginateOne() {
    Paginator<UserM> page = userQuery().orderBy("username").paginate(1, 5);

    assertThat(page.getItems())
        .hasSize(5)
        .map(UserM::getId)
        .contains(USER1_ID, USER2_ID, USER3_ID, USER4_ID, USER5_ID);
  }

  @Test
  void paginateTwo() {
    Paginator<UserM> page = userQuery().orderBy("username").paginate(2, 5);

    assertThat(page.getItems()).hasSize(1).map(UserM::getId).contains(USER6_ID);
  }
}
