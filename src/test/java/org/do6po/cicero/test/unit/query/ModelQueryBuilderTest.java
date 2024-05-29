package org.do6po.cicero.test.unit.query;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.do6po.cicero.query.ModelQueryBuilder;
import org.do6po.cicero.test.integration.model.ProductM;
import org.do6po.cicero.test.integration.model.builder.ProductQB;
import org.junit.jupiter.api.Test;

class ModelQueryBuilderTest extends BaseQueryTest {
  @Test
  void copy() {
    ProductQB productQuery =
        ModelQueryBuilder.query(ProductM.class)
            .select(column1, column2, column3)
            .nullifyRelations()
            .with("relation1", "relation22")
            .distinct()
            .parallel()
            .innerJoin(table2, column1, column2)
            .where(column1, 100)
            .groupBy(column3)
            .orderBy(column1)
            .limit(100)
            .offset(1000);

    ProductQB copy = productQuery.copy();
    assertEquals(productQuery.getWith(), copy.getWith());
    assertEquals(productQuery.isNullifyRelations(), copy.isNullifyRelations());
  }
}
