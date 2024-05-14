package org.do6po.cicero.test.integration.model;

import lombok.Getter;
import org.do6po.cicero.model.BaseModel;
import org.do6po.cicero.test.integration.model.builder.UserProfileQB;

@Getter
public class UserProfileM extends BaseModel<UserProfileM, UserProfileQB> {

  protected final String table = "user_profiles";

  public String getUserId() {
    return getAttribute("user_id");
  }

  public String getFullName() {
    return getAttribute("full_name");
  }
}
