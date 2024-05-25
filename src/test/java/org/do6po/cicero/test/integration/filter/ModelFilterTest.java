package org.do6po.cicero.test.integration.filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.do6po.cicero.enums.PredicateOperatorEnum.LIKE;
import static org.do6po.cicero.query.ModelQueryBuilder.query;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import org.do6po.cicero.annotation.FilterMethod;
import org.do6po.cicero.expression.SqlExpression;
import org.do6po.cicero.filter.BaseModelFilter;
import org.do6po.cicero.filter.ModelFilter;
import org.do6po.cicero.test.integration.BaseDbTest;
import org.do6po.cicero.test.integration.model.ProductM;
import org.do6po.cicero.test.integration.model.ProductM_.Columns;
import org.do6po.cicero.test.integration.model.builder.ProductQB;
import org.junit.jupiter.api.Test;

class ModelFilterTest extends BaseDbTest {

  @Test
  void emptyFilter() {
    ProductFilter filter = new ProductFilter();
    SqlExpression expression = query(ProductM.class).filter(filter).getSqlExpression();

    assertEquals("SELECT * FROM products", expression.getExpression());
  }

  @Test
  void idsFilter() {
    String id1 = UUID.randomUUID().toString();
    String id2 = UUID.randomUUID().toString();
    ProductFilterDto filterDto = ProductFilterDto.builder().ids(List.of(id1, id2)).build();

    ModelFilter<ProductM, ProductQB> filter = new ProductFilter().putFilterObject(filterDto);

    SqlExpression expression = query(ProductM.class).filter(filter).getSqlExpression();

    assertEquals("SELECT * FROM products WHERE id IN (?, ?)", expression.getExpression());
    assertThat(expression.getBindings()).hasSize(2).containsExactly(id1, id2);
  }

  @Test
  void idsFirstAndQuerySecondFilter() {
    String id1 = UUID.randomUUID().toString();
    String id2 = UUID.randomUUID().toString();
    String query = "Some query";
    ProductFilterDto filterDto =
        ProductFilterDto.builder().ids(List.of(id1, id2)).query(query).build();

    ModelFilter<ProductM, ProductQB> filter = new ProductFilter().putFilterObject(filterDto);

    SqlExpression expression = query(ProductM.class).filter(filter).getSqlExpression();

    assertEquals(
        "SELECT * FROM products WHERE id IN (?, ?) AND name LIKE (?)", expression.getExpression());
    assertThat(expression.getBindings()).hasSize(3).containsExactly(id1, id2, "%" + query + "%");
  }

  @Data
  @Builder
  public static class ProductFilterDto {
    private List<String> ids;
    private String query;
  }

  public static class ProductFilter extends BaseModelFilter<ProductM, ProductQB> {

    @FilterMethod(order = 10)
    public void ids(List<String> ids) {
      getBuilder().whereIn(Columns.id, ids);
    }

    @FilterMethod(order = 20)
    public void query(String value) {
      getBuilder().where(Columns.name, LIKE, "%" + value + "%");
    }
  }
}
