package org.do6po.cicero.test.integration.relation;

import static com.github.darrmirr.dbchange.annotation.onclass.DbChangeOnce.ExecutionPhase.AFTER_ALL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.darrmirr.dbchange.annotation.SqlExecutorGetter;
import com.github.darrmirr.dbchange.annotation.onclass.DbChangeOnce;
import java.util.List;
import java.util.Set;
import org.do6po.cicero.collection.ModelList;
import org.do6po.cicero.enums.PredicateOperatorEnum;
import org.do6po.cicero.test.integration.BaseDbTest;
import org.do6po.cicero.test.integration.model.BrandM;
import org.do6po.cicero.test.integration.model.ProductM;
import org.do6po.cicero.test.integration.model.builder.ProductQB;
import org.junit.jupiter.api.Test;

@SqlExecutorGetter(BaseDbTest.DEFAULT_SQL_EXECUTOR)
@DbChangeOnce(sqlQueryFiles = {"sql/products__up.sql"})
@DbChangeOnce(
    sqlQueryFiles = {"sql/products__down.sql"},
    executionPhase = AFTER_ALL)
class ManyToOneTest extends BaseDbTest {

  @Test
  void manyToOne_ProductsBelongToBrand_LazyLoad() {
    startQueryCount();
    ProductM product = productQuery().find(PRODUCT1_ID).orElseThrow();

    BrandM brand = product.getBrand();

    assertQueryCount(2);

    assertEquals(BRAND1_ID, brand.getId());
  }

  @Test
  void manyToMany_ProductsBelongToBrand_loadBatch() {
    startQueryCount();

    ModelList<ProductM, ProductQB> products =
        productQuery()
            .whereKey(Set.of(PRODUCT1_ID, PRODUCT2_ID, PRODUCT3_ID))
            .with("brand")
            .get(ModelList::new);

    assertEquals(BRAND1_ID, products.whereKey(PRODUCT1_ID).getBrand().getId());
    assertEquals(BRAND1_ID, products.whereKey(PRODUCT2_ID).getBrand().getId());
    assertEquals(BRAND2_ID, products.whereKey(PRODUCT3_ID).getBrand().getId());

    assertQueryCount(2);
  }

  @Test
  void manyToMany_findProductWhereHasBrand() {
    startQueryCount();

    List<ProductM> products =
        productQuery()
            .whereHas(ProductM::brand, b -> b.where("name", PredicateOperatorEnum.LIKE, "%best%"))
            .get();

    assertQueryCount(1);

    assertThat(products)
        .hasSize(3)
        .map(ProductM::getId)
        .contains(PRODUCT3_ID, PRODUCT4_ID, PRODUCT5_ID);
  }
}
