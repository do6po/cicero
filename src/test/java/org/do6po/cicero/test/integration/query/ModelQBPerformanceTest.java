package org.do6po.cicero.test.integration.query;

import static com.github.darrmirr.dbchange.annotation.onclass.DbChangeOnce.ExecutionPhase.AFTER_ALL;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.darrmirr.dbchange.annotation.SqlExecutorGetter;
import com.github.darrmirr.dbchange.annotation.onclass.DbChangeOnce;
import java.util.Set;
import org.do6po.cicero.collection.ModelList;
import org.do6po.cicero.test.integration.BaseDbTest;
import org.do6po.cicero.test.integration.model.ProductM;
import org.do6po.cicero.test.integration.model.builder.ProductQB;
import org.junit.jupiter.api.Test;

@SqlExecutorGetter(BaseDbTest.DEFAULT_SQL_EXECUTOR)
@DbChangeOnce(sqlQueryFiles = {"sql/products__up.sql"})
@DbChangeOnce(
    sqlQueryFiles = {"sql/products__down.sql"},
    executionPhase = AFTER_ALL)
class ModelQBPerformanceTest extends BaseDbTest {

  @Test
  void multiGet() {
    long startTime = System.currentTimeMillis();

    startQueryCount();

    int iterations = 1000;
    for (int i = 0; i < iterations; i++) {
      ModelList<ProductM, ProductQB> products =
          productQuery()
              .withCount(ProductM::categories, "category_count")
              .whereKey(Set.of(PRODUCT1_ID, PRODUCT2_ID, PRODUCT3_ID, PRODUCT4_ID))
              .get(ModelList::new);

      assertThat(products).hasSize(4);
    }

    assertQueryCount(iterations);

    int maxDuration = 1000;
    assertThat(System.currentTimeMillis() - startTime)
        .withFailMessage(
            "Performance is low. Duration should be less than %sms".formatted(maxDuration))
        .isLessThan(maxDuration);
  }
}
