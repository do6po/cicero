package org.do6po.cicero.test.integration.model;

import lombok.Getter;
import org.do6po.cicero.model.BaseModel;
import org.do6po.cicero.test.integration.model.builder.OrderQB;

@Getter
public class OrderM extends BaseModel<OrderM, OrderQB> {
  protected final String table = "orders";

  public String getId() {
    return getAttribute("id");
  }
}
