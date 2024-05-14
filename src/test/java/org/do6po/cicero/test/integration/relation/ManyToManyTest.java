package org.do6po.cicero.test.integration.relation;

import static com.github.darrmirr.dbchange.annotation.onclass.DbChangeOnce.ExecutionPhase.AFTER_ALL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.do6po.cicero.enums.PredicateOperatorEnum.LIKE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.darrmirr.dbchange.annotation.SqlExecutorGetter;
import com.github.darrmirr.dbchange.annotation.onclass.DbChangeOnce;
import java.util.List;
import java.util.Set;
import org.do6po.cicero.collection.ModelList;
import org.do6po.cicero.test.integration.BaseDbTest;
import org.do6po.cicero.test.integration.model.CategoryM;
import org.do6po.cicero.test.integration.model.ProductM;
import org.do6po.cicero.test.integration.model.builder.ProductQB;
import org.junit.jupiter.api.Test;

@SqlExecutorGetter("defaultSqlExecutor")
@DbChangeOnce(sqlQueryFiles = {"sql/products__up.sql"})
@DbChangeOnce(
    sqlQueryFiles = {"sql/products__down.sql"},
    executionPhase = AFTER_ALL)
class ManyToManyTest extends BaseDbTest {

  @Test
  void manyToMany_lazyLoad() {
    startQueryCount();

    ProductM product = productQuery().find(PRODUCT1_ID).orElseThrow();

    List<CategoryM> categories = product.getCategories();

    assertQueryCount(2);

    assertThat(categories).hasSize(2).map(CategoryM::getId).contains(CATEGORY1_ID, CATEGORY2_ID);
  }

  @Test
  void manyToMany_loadBatch() {
    startQueryCount();

    ModelList<ProductM, ProductQB> products =
        productQuery()
            .whereIn("id", Set.of(PRODUCT1_ID, PRODUCT2_ID, PRODUCT3_ID))
            .with("categories")
            .get(ModelList::new);

    assertQueryCount(2);

    assertThat(products).hasSize(3);

    assertThat(products.whereKey(PRODUCT1_ID).getCategories())
        .hasSize(2)
        .map(CategoryM::getId)
        .contains(CATEGORY1_ID, CATEGORY2_ID);

    assertThat(products.whereKey(PRODUCT2_ID).getCategories())
        .hasSize(3)
        .map(CategoryM::getId)
        .contains(CATEGORY1_ID, CATEGORY2_ID, CATEGORY3_ID);

    assertThat(products.whereKey(PRODUCT3_ID).getCategories())
        .hasSize(1)
        .map(CategoryM::getId)
        .contains(CATEGORY3_ID);

    assertQueryCount(2);
  }

  @Test
  void manyToMany_whereHas() {
    startQueryCount();

    ModelList<ProductM, ProductQB> products =
        productQuery()
            .whereHas(ProductM::categories, b -> b.where("name", LIKE, "%second%"))
            .get(ModelList::new);

    assertQueryCount(1);

    assertThat(products).hasSize(2).map(ProductM::getId).contains(PRODUCT1_ID, PRODUCT2_ID);
  }

  @Test
  void manyToMany_withCount() {
    startQueryCount();

    String categoryCountAttribute = "category_count";
    ModelList<ProductM, ProductQB> products =
        productQuery()
            .withCount(ProductM::categories, categoryCountAttribute)
            .whereKey(Set.of(PRODUCT1_ID, PRODUCT2_ID, PRODUCT3_ID, PRODUCT4_ID))
            .get(ModelList::new);

    assertQueryCount(1);

    assertEquals(2, (Long) products.whereKey(PRODUCT1_ID).getAttribute(categoryCountAttribute));
    assertEquals(3, (Long) products.whereKey(PRODUCT2_ID).getAttribute(categoryCountAttribute));
    assertEquals(1, (Long) products.whereKey(PRODUCT3_ID).getAttribute(categoryCountAttribute));
    assertEquals(0, (Long) products.whereKey(PRODUCT4_ID).getAttribute(categoryCountAttribute));
  }
}
