package org.do6po.cicero.test.integration.model;

import lombok.Getter;
import org.do6po.cicero.annotation.ModelProjection;
import org.do6po.cicero.model.BaseModel;
import org.do6po.cicero.test.integration.model.MediaM_.Columns;
import org.do6po.cicero.test.integration.model.builder.MediaQB;

@Getter
@ModelProjection(
    table = "media",
    columns = {
      "id",
      "reference_type",
      "reference_id",
      "collection",
      "name",
      "description",
      "created_at",
      "updated_at",
    })
public class MediaM extends BaseModel<MediaM, MediaQB> {
  protected final String table = MediaM_.table;

  public String getId() {
    return getAttribute(Columns.id);
  }
}
