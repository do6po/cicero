package org.do6po.cicero.test.integration.model.builder;

import org.do6po.cicero.query.ModelQueryBuilder;
import org.do6po.cicero.test.integration.model.OrderM;

public class OrderQB extends ModelQueryBuilder<OrderM, OrderQB> {

  public OrderQB() {
    super(OrderM.class);
  }
}
