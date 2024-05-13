package org.do6po.cicero.test.integration.model.builder;

import org.do6po.cicero.query.ModelQueryBuilder;
import org.do6po.cicero.test.integration.model.ProductM;

public class ProductQB extends ModelQueryBuilder<ProductM, ProductQB> {
  public ProductQB() {
    super(ProductM.class);
  }
}
