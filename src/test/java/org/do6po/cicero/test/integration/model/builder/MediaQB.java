package org.do6po.cicero.test.integration.model.builder;

import org.do6po.cicero.query.ModelQueryBuilder;
import org.do6po.cicero.test.integration.model.MediaM;

public class MediaQB extends ModelQueryBuilder<MediaM, MediaQB> {
  public MediaQB() {
    super(MediaM.class);
  }

  public MediaQB whereCollection(String collection) {
    return where("collection", collection);
  }
}
