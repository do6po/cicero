package org.do6po.cicero.test.integration.model;

import java.util.List;
import lombok.Getter;
import org.do6po.cicero.model.BaseModel;
import org.do6po.cicero.relation.MorphMany;
import org.do6po.cicero.relation.Relation;
import org.do6po.cicero.test.integration.model.builder.BrandQB;
import org.do6po.cicero.test.integration.model.builder.MediaQB;

@Getter
public class BrandM extends BaseModel<BrandM, BrandQB> {
  protected final String table = "brands";

  public String getId() {
    return getAttribute("id");
  }

  public Relation<BrandM, MediaM, MediaQB, List<MediaM>> media() {
    return new MorphMany<>(this, "id", MediaM.class, "reference_type", "reference_id");
  }

  public List<MediaM> getMedia() {
    return getRelation(BrandM::media);
  }
}
