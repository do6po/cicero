package org.do6po.cicero.test.integration.model;

import lombok.Getter;
import org.do6po.cicero.model.BaseModel;
import org.do6po.cicero.relation.HasOne;
import org.do6po.cicero.relation.Relation;
import org.do6po.cicero.test.integration.model.builder.UserProfileQB;
import org.do6po.cicero.test.integration.model.builder.UserQB;

@Getter
public class UserM extends BaseModel<UserM, UserQB> {

  protected final String table = "users";

  public String getId() {
    return getAttribute("id");
  }

  public Relation<UserM, UserProfileM, UserProfileQB, UserProfileM> profile() {
    return new HasOne<>(this, "id", UserProfileM.class, "user_id");
  }

  public UserProfileM getProfile() {
    return getRelation(UserM::profile);
  }
}
