package org.do6po.cicero.test.integration.query;

import static com.github.darrmirr.dbchange.annotation.onclass.DbChangeOnce.ExecutionPhase.AFTER_ALL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.github.darrmirr.dbchange.annotation.SqlExecutorGetter;
import com.github.darrmirr.dbchange.annotation.onclass.DbChangeOnce;
import java.util.List;
import org.do6po.cicero.iterator.ChunkIterator;
import org.do6po.cicero.test.integration.BaseDbTest;
import org.do6po.cicero.test.integration.model.ProductM;
import org.do6po.cicero.test.integration.model.ProductM_.Relations;
import org.do6po.cicero.test.integration.model.builder.ProductQB;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@SqlExecutorGetter(BaseDbTest.DEFAULT_SQL_EXECUTOR)
@DbChangeOnce(sqlQueryFiles = {"sql/products__up.sql"})
@DbChangeOnce(
    sqlQueryFiles = {"sql/products__down.sql"},
    executionPhase = AFTER_ALL)
class ModelChunkIteratorTest extends BaseDbTest {

  @Test
  void chunkIterator_withRelationLoad() {
    startQueryCount();

    try (ChunkIterator<ProductM, ProductQB> modelChunkIterator =
        productQuery().with(Relations.brand, Relations.categories).chunk(3)) {

      assertQueryCount(1);

      assertTrue(modelChunkIterator.hasNext());
      List<ProductM> chunk1 = modelChunkIterator.next();
      assertThat(chunk1).hasSize(3);

      // + 2 relations
      assertQueryCount(3);

      assertTrue(modelChunkIterator.hasNext());
      List<ProductM> chunk2 = modelChunkIterator.next();
      assertThat(chunk2).hasSize(3);

      // + 2 relations
      assertQueryCount(5);

      assertTrue(modelChunkIterator.hasNext());
      List<ProductM> chunk3 = modelChunkIterator.next();
      assertThat(chunk3).hasSize(1);

      // + 2 relations
      assertQueryCount(7);

      assertFalse(modelChunkIterator.hasNext());
    } catch (Exception e) {
      fail("Unexpected exception!");
    }

    stopQueryCount();
  }
}
