package org.do6po.cicero.test.integration.model;

import lombok.Getter;
import org.do6po.cicero.annotation.ModelProjection;
import org.do6po.cicero.model.BaseModel;
import org.do6po.cicero.test.integration.model.UserProfileM_.Columns;
import org.do6po.cicero.test.integration.model.builder.UserProfileQB;

@Getter
@ModelProjection(
    table = "user_profiles",
    columns = {
      "user_id",
      "full_name",
      "last_name",
      "updated_at",
    })
public class UserProfileM extends BaseModel<UserProfileM, UserProfileQB> {
  protected final String table = UserProfileM_.table;

  public String getUserId() {
    return getAttribute(Columns.userId);
  }

  public String getFullName() {
    return getAttribute(Columns.fullName);
  }
}
