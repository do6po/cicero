package org.do6po.cicero.test.integration.model.builder;

import org.do6po.cicero.query.ModelQueryBuilder;
import org.do6po.cicero.test.integration.model.CategoryM;

public class CategoryQB extends ModelQueryBuilder<CategoryM, CategoryQB> {
  public CategoryQB() {
    super(CategoryM.class);
  }
}
