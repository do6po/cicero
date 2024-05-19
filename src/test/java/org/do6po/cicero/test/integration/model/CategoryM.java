package org.do6po.cicero.test.integration.model;

import lombok.Getter;
import org.do6po.cicero.annotation.ModelProjection;
import org.do6po.cicero.model.BaseModel;
import org.do6po.cicero.test.integration.model.CategoryM_.Columns;
import org.do6po.cicero.test.integration.model.builder.CategoryQB;

@Getter
@ModelProjection(
    table = "categories",
    columns = {
      "id",
      "name",
      "created_at",
      "updated_at",
    })
public class CategoryM extends BaseModel<CategoryM, CategoryQB> {
  protected final String table = CategoryM_.table;

  public String getId() {
    return getAttribute(Columns.id);
  }
}
