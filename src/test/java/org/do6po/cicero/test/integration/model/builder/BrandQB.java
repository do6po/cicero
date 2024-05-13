package org.do6po.cicero.test.integration.model.builder;

import org.do6po.cicero.query.ModelQueryBuilder;
import org.do6po.cicero.test.integration.model.BrandM;

public class BrandQB extends ModelQueryBuilder<BrandM, BrandQB> {
  public BrandQB() {
    super(BrandM.class);
  }
}
