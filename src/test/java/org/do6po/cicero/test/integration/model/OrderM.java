package org.do6po.cicero.test.integration.model;

import lombok.Getter;
import org.do6po.cicero.annotation.ModelProjection;
import org.do6po.cicero.model.BaseModel;
import org.do6po.cicero.test.integration.model.OrderM_.Columns;
import org.do6po.cicero.test.integration.model.builder.OrderQB;

@Getter
@ModelProjection(
    table = "orders",
    columns = {
      "id",
      "user_id",
      "description",
      "created_at",
      "updated_at",
    })
public class OrderM extends BaseModel<OrderM, OrderQB> {
  protected final String table = OrderM_.table;

  public String getId() {
    return getAttribute(Columns.id);
  }
}
