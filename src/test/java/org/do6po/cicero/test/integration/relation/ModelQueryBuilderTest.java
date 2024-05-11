package org.do6po.cicero.test.integration.relation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.do6po.cicero.enums.PredicateOperatorEnum.LIKE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.darrmirr.dbchange.DbChangeExtension;
import com.github.darrmirr.dbchange.annotation.SqlExecutorGetter;
import com.github.darrmirr.dbchange.annotation.onclass.DbChangeOnce;
import com.github.darrmirr.dbchange.annotation.onclass.DbChangeOnce.ExecutionPhase;
import java.util.List;
import java.util.Set;
import org.do6po.cicero.pagination.Paginator;
import org.do6po.cicero.test.integration.BaseDbTest;
import org.do6po.cicero.test.integration.model.UserM;
import org.do6po.cicero.test.integration.model.UserProfileM;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DbChangeExtension.class)
@SqlExecutorGetter("defaultSqlExecutor")
@DbChangeOnce(sqlQueryFiles = "sql/users__up.sql")
@DbChangeOnce(sqlQueryFiles = "sql/users__down.sql", executionPhase = ExecutionPhase.AFTER_ALL)
class ModelQueryBuilderTest extends BaseDbTest {

  public static final String USER1_ID = "4fbcfc50-7dda-4c1c-b358-30e70cb8b6d8";
  public static final String USER2_ID = "2249fa0d-766d-41e5-9b7a-2ca0961d1ab6";
  public static final String USER3_ID = "84409556-a609-4375-b5b6-44678009d193";
  public static final String USER4_ID = "2a03be2d-386d-45d6-bba2-8ce8afd81c5f";
  public static final String USER5_ID = "c0ec4c02-e22d-457a-982f-0f2b719d8142";
  public static final String USER6_ID = "6e8393d1-1f0d-4d11-9708-2ef3ad1ae060";

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

    assertThat(page.getItems()).hasSize(5).map(UserM::getId)
        .contains(USER1_ID, USER2_ID, USER3_ID, USER4_ID, USER5_ID);
  }

  @Test
  void paginateTwo() {
    Paginator<UserM> page = userQuery().orderBy("username").paginate(2, 5);

    assertThat(page.getItems()).hasSize(1).map(UserM::getId).contains(USER6_ID);
  }

  @Test
  void hasOne() {
    UserM user = userQuery().find(USER1_ID).orElseThrow();

    UserProfileM profile = user.getProfile();
    assertThat(profile).isNotNull();
    assertEquals(USER1_ID, profile.getUserId());
    assertEquals("full name 1", profile.getFullName());
  }

  @Test
  void hasOneBatch() {
    List<UserM> users = userQuery().where("password", LIKE, "%456%").with("profile").get();

    assertThat(users).hasSize(4).map(UserM::getId).contains(USER1_ID, USER2_ID, USER3_ID, USER4_ID);

    List<UserProfileM> profiles = users.stream().map(UserM::getProfile).toList();
    assertThat(profiles).hasSize(4).map(UserProfileM::getUserId)
        .contains(USER1_ID, USER2_ID, USER3_ID, USER4_ID);
  }
}
