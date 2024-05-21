package org.do6po.cicero.test.integration.model;

import java.util.List;
import lombok.Getter;
import org.do6po.cicero.annotation.ModelProjection;
import org.do6po.cicero.model.BaseModel;
import org.do6po.cicero.relation.MorphMany;
import org.do6po.cicero.relation.Relation;
import org.do6po.cicero.test.integration.model.BrandM_.Columns;
import org.do6po.cicero.test.integration.model.builder.BrandQB;
import org.do6po.cicero.test.integration.model.builder.MediaQB;

@Getter
@ModelProjection(
    table = "brands",
    columns = {
      "id",
      "name",
      "description",
      "created_at",
      "updated_at",
    })
public class BrandM extends BaseModel<BrandM, BrandQB> {
  protected final String table = BrandM_.table;

  public String getId() {
    return getAttribute(Columns.id);
  }

  public Relation<BrandM, MediaM, MediaQB, List<MediaM>> media() {
    return new MorphMany<>(
        this, Columns.id, MediaM.class, MediaM_.Columns.referenceType, MediaM_.Columns.referenceId);
  }

  public List<MediaM> getMedia() {
    return getRelation(BrandM::media);
  }
}
