package org.do6po.cicero.test.integration.model.builder;

import org.do6po.cicero.query.ModelQueryBuilder;
import org.do6po.cicero.test.integration.model.UserProfileM;

public class UserProfileQB extends ModelQueryBuilder<UserProfileM, UserProfileQB> {

  public UserProfileQB() {
    super(UserProfileM.class);
  }
}
