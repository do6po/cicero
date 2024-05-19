package org.do6po.cicero.test.integration.model;

import java.util.List;
import lombok.Getter;
import org.do6po.cicero.annotation.ModelProjection;
import org.do6po.cicero.model.BaseModel;
import org.do6po.cicero.relation.HasMany;
import org.do6po.cicero.relation.HasOne;
import org.do6po.cicero.relation.Relation;
import org.do6po.cicero.test.integration.model.UserM_.Columns;
import org.do6po.cicero.test.integration.model.builder.OrderQB;
import org.do6po.cicero.test.integration.model.builder.UserProfileQB;
import org.do6po.cicero.test.integration.model.builder.UserQB;

@Getter
@ModelProjection(
    table = "users",
    columns = {
      "id",
      "username",
      "password",
      "created_at",
      "updated_at",
    })
public class UserM extends BaseModel<UserM, UserQB> {
  protected final String table = UserM_.table;

  public String getId() {
    return getAttribute(Columns.id);
  }

  public Relation<UserM, OrderM, OrderQB, List<OrderM>> orders() {
    return new HasMany<>(this, Columns.id, OrderM.class, OrderM_.Columns.userId);
  }

  public Relation<UserM, UserProfileM, UserProfileQB, UserProfileM> profile() {
    return new HasOne<>(this, Columns.id, UserProfileM.class, UserProfileM_.Columns.userId);
  }

  public UserProfileM getProfile() {
    return getRelation(UserM::profile);
  }

  public List<OrderM> getOrders() {
    return getRelation(UserM::orders);
  }
}
