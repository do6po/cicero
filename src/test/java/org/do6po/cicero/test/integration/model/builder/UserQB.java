package org.do6po.cicero.test.integration.model.builder;

import org.do6po.cicero.query.ModelQueryBuilder;
import org.do6po.cicero.test.integration.model.UserM;

public class UserQB extends ModelQueryBuilder<UserM, UserQB> {

  public UserQB() {
    super(UserM.class);
  }
}
