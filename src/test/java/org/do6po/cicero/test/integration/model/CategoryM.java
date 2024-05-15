package org.do6po.cicero.test.integration.model;

import lombok.Getter;
import org.do6po.cicero.model.BaseModel;
import org.do6po.cicero.test.integration.model.builder.CategoryQB;

@Getter
public class CategoryM extends BaseModel<CategoryM, CategoryQB> {
  protected final String table = "categories";

  public String getId() {
    return getAttribute("id");
  }
}
