package org.do6po.cicero.test.integration.model;

import java.util.List;
import lombok.Getter;
import org.do6po.cicero.model.BaseModel;
import org.do6po.cicero.relation.MorphMany;
import org.do6po.cicero.relation.Relation;
import org.do6po.cicero.test.integration.model.builder.MediaQB;
import org.do6po.cicero.test.integration.model.builder.ProductQB;

@Getter
public class ProductM extends BaseModel<ProductM, ProductQB> {
  protected final String table = "products";

  public String getId() {
    return getAttribute("id");
  }

  public Relation<ProductM, MediaM, MediaQB, List<MediaM>> media() {
    return new MorphMany<>(this, "id", MediaM.class, "reference_type", "reference_id");
  }

  public List<MediaM> getMedia() {
    return getRelation(ProductM::media);
  }
}
