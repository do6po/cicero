package org.do6po.cicero.test.integration.relation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.do6po.cicero.enums.PredicateOperatorEnum.LIKE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.darrmirr.dbchange.annotation.SqlExecutorGetter;
import com.github.darrmirr.dbchange.annotation.onclass.DbChangeOnce;
import com.github.darrmirr.dbchange.annotation.onclass.DbChangeOnce.ExecutionPhase;
import java.util.List;
import org.do6po.cicero.test.integration.BaseDbTest;
import org.do6po.cicero.test.integration.model.UserM;
import org.do6po.cicero.test.integration.model.UserProfileM;
import org.junit.jupiter.api.Test;

@SqlExecutorGetter(BaseDbTest.DEFAULT_SQL_EXECUTOR)
@DbChangeOnce(sqlQueryFiles = "sql/users__up.sql")
@DbChangeOnce(sqlQueryFiles = "sql/users__down.sql", executionPhase = ExecutionPhase.AFTER_ALL)
class HasOneTest extends BaseDbTest {

  @Test
  void hasOne_lazyLoad() {
    startQueryCount();

    UserM user = userQuery().find(USER1_ID).orElseThrow();

    UserProfileM profile = user.getProfile();

    assertQueryCount(2);

    assertThat(profile).isNotNull();
    assertEquals(USER1_ID, profile.getUserId());
    assertEquals("full name 1", profile.getFullName());
  }

  @Test
  void hasOneBatch() {
    startQueryCount();

    List<UserM> users = userQuery().where("password", LIKE, "%456%").with("profile").get();

    assertQueryCount(2);

    assertThat(users).hasSize(4).map(UserM::getId).contains(USER1_ID, USER2_ID, USER3_ID, USER4_ID);

    List<UserProfileM> profiles = users.stream().map(UserM::getProfile).toList();
    assertThat(profiles)
        .hasSize(4)
        .map(UserProfileM::getUserId)
        .contains(USER1_ID, USER2_ID, USER3_ID, USER4_ID);

    assertQueryCount(2);
  }
}
