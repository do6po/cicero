package org.do6po.cicero.test.integration.model;

import lombok.Getter;
import org.do6po.cicero.model.BaseModel;
import org.do6po.cicero.test.integration.model.builder.MediaQB;

@Getter
public class MediaM extends BaseModel<MediaM, MediaQB> {
  protected final String table = "media";

  public String getId() {
    return getAttribute("id");
  }
}
