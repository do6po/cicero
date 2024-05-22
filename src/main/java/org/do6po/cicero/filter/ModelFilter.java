package org.do6po.cicero.filter;

import org.do6po.cicero.model.BaseModel;
import org.do6po.cicero.query.ModelQueryBuilder;

public interface ModelFilter<M extends BaseModel<M, B>, B extends ModelQueryBuilder<M, B>> {
  void fill(B builder);
}
