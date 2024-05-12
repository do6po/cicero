package org.do6po.cicero.test.integration.relation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.do6po.cicero.enums.PredicateOperatorEnum.ILIKE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.darrmirr.dbchange.DbChangeExtension;
import com.github.darrmirr.dbchange.annotation.SqlExecutorGetter;
import com.github.darrmirr.dbchange.annotation.onclass.DbChangeOnce;
import com.github.darrmirr.dbchange.annotation.onclass.DbChangeOnce.ExecutionPhase;
import java.util.List;
import java.util.Set;
import org.do6po.cicero.collection.ModelList;
import org.do6po.cicero.test.integration.BaseDbTest;
import org.do6po.cicero.test.integration.model.OrderM;
import org.do6po.cicero.test.integration.model.UserM;
import org.do6po.cicero.test.integration.model.builder.UserQB;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DbChangeExtension.class)
@SqlExecutorGetter("defaultSqlExecutor")
@DbChangeOnce(
    sqlQueryFiles = {
      "sql/users__up.sql",
      "sql/orders__up.sql",
    })
@DbChangeOnce(
    sqlQueryFiles = {
      "sql/users__down.sql",
      "sql/orders__down.sql",
    },
    executionPhase = ExecutionPhase.AFTER_ALL)
class HasManyTest extends BaseDbTest {

  public static final String ORDER_COUNT = "order_count";

  @Test
  void hasMany() {
    UserM user = userQuery().find(USER2_ID).orElseThrow();

    List<OrderM> orders = user.getOrders();

    assertThat(orders)
        .hasSize(3)
        .map(OrderM::getId)
        .contains(USER2_ORDER1_ID, USER2_ORDER2_ID, USER2_ORDER3_ID);
  }

  @Test
  void hasManyBatch() {
    ModelList<UserM, UserQB> users =
        userQuery()
            .with("orders")
            .whereKey(Set.of(USER1_ID, USER2_ID, USER3_ID))
            .get(ModelList::new);

    assertThat(users).hasSize(3);

    assertThat(users.whereKey(USER1_ID).getOrders())
        .hasSize(2)
        .map(OrderM::getId)
        .contains(USER1_ORDER1_ID, USER1_ORDER2_ID);

    assertThat(users.whereKey(USER2_ID).getOrders())
        .hasSize(3)
        .map(OrderM::getId)
        .contains(USER2_ORDER1_ID, USER2_ORDER2_ID, USER2_ORDER3_ID);

    assertThat(users.whereKey(USER3_ID).getOrders()).isEmpty();
  }

  @Test
  void findUsersWhereHasSomeTextInOrderDescription() {
    ModelList<UserM, UserQB> users =
        userQuery()
            .whereHas(UserM::orders, b -> b.where("description", ILIKE, "%ipsum%"))
            .get(ModelList::new);

    assertThat(users).hasSize(2).map(UserM::getId).contains(USER1_ID, USER2_ID);
  }

  @Test
  void withOrderCount() {
    ModelList<UserM, UserQB> users =
        userQuery().withCount(UserM::orders, ORDER_COUNT).get(ModelList::new);

    assertEquals(2, (Long) users.whereKey(USER1_ID).getAttribute(ORDER_COUNT));
    assertEquals(3, (Long) users.whereKey(USER2_ID).getAttribute(ORDER_COUNT));
    assertEquals(0, (Long) users.whereKey(USER3_ID).getAttribute(ORDER_COUNT));
    assertEquals(0, (Long) users.whereKey(USER4_ID).getAttribute(ORDER_COUNT));
    assertEquals(0, (Long) users.whereKey(USER5_ID).getAttribute(ORDER_COUNT));
    assertEquals(1, (Long) users.whereKey(USER6_ID).getAttribute(ORDER_COUNT));
  }
}
