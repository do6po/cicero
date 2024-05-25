package org.do6po.cicero.test.integration.relation;

import static com.github.darrmirr.dbchange.annotation.onclass.DbChangeOnce.ExecutionPhase.AFTER_ALL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.darrmirr.dbchange.annotation.SqlExecutorGetter;
import com.github.darrmirr.dbchange.annotation.onclass.DbChangeOnce;
import java.util.List;
import java.util.Set;
import org.do6po.cicero.collection.ModelList;
import org.do6po.cicero.test.integration.BaseDbTest;
import org.do6po.cicero.test.integration.model.MediaM;
import org.do6po.cicero.test.integration.model.ProductM;
import org.do6po.cicero.test.integration.model.builder.ProductQB;
import org.junit.jupiter.api.Test;

@SqlExecutorGetter(BaseDbTest.DEFAULT_SQL_EXECUTOR)
@DbChangeOnce(sqlQueryFiles = {"sql/products__up.sql"})
@DbChangeOnce(
    sqlQueryFiles = {"sql/products__down.sql"},
    executionPhase = AFTER_ALL)
class MorphManyTest extends BaseDbTest {

  @Test
  void morphMany_ProductHasMedia_lazyLoad() {
    startQueryCount();

    ProductM product = productQuery().find(PRODUCT1_ID).orElseThrow();
    List<MediaM> mediaList = product.getMedia();

    assertQueryCount(2);

    assertThat(mediaList).hasSize(3).map(MediaM::getId).contains(MEDIA1_ID, MEDIA2_ID, MEDIA3_ID);
  }

  @Test
  void morphMany_ProductsHasMedia() {
    startQueryCount();

    ModelList<ProductM, ProductQB> products =
        productQuery().with("media").whereKey(Set.of(PRODUCT1_ID, PRODUCT2_ID)).get(ModelList::new);

    assertQueryCount(2);

    assertThat(products).hasSize(2);

    assertThat(products.whereKey(PRODUCT1_ID).getMedia())
        .hasSize(3)
        .map(MediaM::getId)
        .contains(MEDIA1_ID, MEDIA2_ID, MEDIA3_ID);

    assertThat(products.whereKey(PRODUCT2_ID).getMedia())
        .hasSize(1)
        .map(MediaM::getId)
        .contains(MEDIA4_ID);

    assertQueryCount(2);
  }

  @Test
  void morphMany_WhereHas() {
    startQueryCount();

    ModelList<ProductM, ProductQB> products =
        productQuery()
            .whereHas(ProductM::media, b -> b.whereCollection("collection3"))
            .get(ModelList::new);

    assertQueryCount(1);

    assertThat(products).hasSize(1).map(ProductM::getId).contains(PRODUCT2_ID);
  }

  @Test
  void withCount() {
    startQueryCount();

    ModelList<ProductM, ProductQB> products =
        productQuery().withCount(ProductM::media, "media_count").get(ModelList::new);

    assertQueryCount(1);

    assertEquals(3, (Long) products.whereKey(PRODUCT1_ID).getAttribute("media_count"));
    assertEquals(1, (Long) products.whereKey(PRODUCT2_ID).getAttribute("media_count"));
    assertEquals(0, (Long) products.whereKey(PRODUCT3_ID).getAttribute("media_count"));
    assertEquals(0, (Long) products.whereKey(PRODUCT4_ID).getAttribute("media_count"));
    assertEquals(0, (Long) products.whereKey(PRODUCT5_ID).getAttribute("media_count"));
    assertEquals(0, (Long) products.whereKey(PRODUCT6_ID).getAttribute("media_count"));
    assertEquals(0, (Long) products.whereKey(PRODUCT7_ID).getAttribute("media_count"));
  }
}
