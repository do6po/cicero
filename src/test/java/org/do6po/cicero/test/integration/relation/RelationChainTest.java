package org.do6po.cicero.test.integration.relation;

import static com.github.darrmirr.dbchange.annotation.onclass.DbChangeOnce.ExecutionPhase.AFTER_ALL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.do6po.cicero.utils.DotUtil.dot;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.github.darrmirr.dbchange.annotation.SqlExecutorGetter;
import com.github.darrmirr.dbchange.annotation.onclass.DbChangeOnce;
import java.util.Set;
import org.do6po.cicero.collection.ModelList;
import org.do6po.cicero.test.integration.BaseDbTest;
import org.do6po.cicero.test.integration.model.BrandM;
import org.do6po.cicero.test.integration.model.BrandM_;
import org.do6po.cicero.test.integration.model.ProductM;
import org.do6po.cicero.test.integration.model.ProductM_.Relations;
import org.do6po.cicero.test.integration.model.builder.ProductQB;
import org.junit.jupiter.api.Test;

@SqlExecutorGetter(BaseDbTest.DEFAULT_SQL_EXECUTOR)
@DbChangeOnce(sqlQueryFiles = {"sql/products__up.sql"})
@DbChangeOnce(
    sqlQueryFiles = {"sql/products__down.sql"},
    executionPhase = AFTER_ALL)
class RelationChainTest extends BaseDbTest {

  @Test
  void chain() {
    startQueryCount();

    ModelList<ProductM, ProductQB> products =
        productQuery()
            .with(dot(Relations.brand, BrandM_.Relations.media), Relations.categories)
            .whereKey(Set.of(PRODUCT1_ID, PRODUCT2_ID, PRODUCT3_ID))
            .get(ModelList::new);

    assertQueryCount(4);

    assertThat(products).hasSize(3);

    ProductM product1 = products.whereKey(PRODUCT1_ID);
    BrandM brand1 = product1.getBrand();
    assertThat(brand1.getMedia()).hasSize(1);
    assertThat(product1.getCategories()).hasSize(2);

    ProductM product2 = products.whereKey(PRODUCT2_ID);
    BrandM brand2 = product2.getBrand();
    assertThat(brand2.getMedia()).hasSize(1);
    assertThat(product2.getCategories()).hasSize(3);

    ProductM product3 = products.whereKey(PRODUCT3_ID);
    BrandM brand3 = product3.getBrand();
    assertThat(brand3.getMedia()).isEmpty();
    assertThat(product3.getCategories()).hasSize(1);

    assertQueryCount(4);
  }

  @Test
  void chainWithNullifyRelations() {
    startQueryCount();

    ModelList<ProductM, ProductQB> products =
        productQuery()
            .with(dot(Relations.brand, BrandM_.Relations.media))
            .nullifyRelations()
            .whereKey(Set.of(PRODUCT1_ID, PRODUCT2_ID, PRODUCT3_ID))
            .get(ModelList::new);

    assertQueryCount(3);

    assertThat(products).hasSize(3);

    ProductM product1 = products.whereKey(PRODUCT1_ID);
    BrandM brand1 = product1.getBrand();
    assertThat(brand1.getMedia()).hasSize(1);
    assertNull(product1.getCategories());

    ProductM product2 = products.whereKey(PRODUCT2_ID);
    BrandM brand2 = product2.getBrand();
    assertThat(brand2.getMedia()).hasSize(1);
    assertNull(product2.getCategories());

    ProductM product3 = products.whereKey(PRODUCT3_ID);
    BrandM brand3 = product3.getBrand();
    assertThat(brand3.getMedia()).isEmpty();
    assertNull(product3.getCategories());

    assertQueryCount(3);
  }
}
