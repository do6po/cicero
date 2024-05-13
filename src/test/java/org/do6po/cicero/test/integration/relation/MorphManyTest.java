package org.do6po.cicero.test.integration.relation;

import static com.github.darrmirr.dbchange.annotation.onclass.DbChangeOnce.ExecutionPhase.AFTER_ALL;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.darrmirr.dbchange.DbChangeExtension;
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
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DbChangeExtension.class)
@SqlExecutorGetter("defaultSqlExecutor")
@DbChangeOnce(sqlQueryFiles = {"sql/products__up.sql"})
@DbChangeOnce(
    sqlQueryFiles = {"sql/products__down.sql"},
    executionPhase = AFTER_ALL)
class MorphManyTest extends BaseDbTest {

  @Test
  void morphMany_ProductHasMedia() {
    ProductM product = productQuery().find(PRODUCT1_ID).orElseThrow();

    List<MediaM> mediaList = product.getMedia();

    assertThat(mediaList).hasSize(3).map(MediaM::getId).contains(MEDIA1_ID, MEDIA2_ID, MEDIA3_ID);
  }

  @Test
  void morphMany_ProductsHasMedia() {
    ModelList<ProductM, ProductQB> products =
        productQuery().with("media").whereKey(Set.of(PRODUCT1_ID, PRODUCT2_ID)).get(ModelList::new);

    assertThat(products).hasSize(2);

    assertThat(products.whereKey(PRODUCT1_ID).getMedia())
        .hasSize(3)
        .map(MediaM::getId)
        .contains(MEDIA1_ID, MEDIA2_ID, MEDIA3_ID);

    assertThat(products.whereKey(PRODUCT2_ID).getMedia())
        .hasSize(1)
        .map(MediaM::getId)
        .contains(MEDIA4_ID);
  }

  @Test
  void morphMany_WhereHas() {
    ModelList<ProductM, ProductQB> products =
        productQuery()
            .with("media")
            .whereHas(ProductM::media, b -> b.whereCollection("collection3"))
            .get(ModelList::new);

    assertThat(products).hasSize(1).map(ProductM::getId).contains(PRODUCT2_ID);
  }
}
