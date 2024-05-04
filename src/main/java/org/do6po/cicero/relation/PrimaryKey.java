package org.do6po.cicero.relation;

import java.util.Collection;
import java.util.Map;
import org.do6po.cicero.model.BaseModel;
import org.do6po.cicero.query.ModelQueryBuilder;

public interface PrimaryKey<M extends BaseModel<M, B>, B extends ModelQueryBuilder<M, B>> {

  Object extract(M model);

  void whereKey(B builder, Object key);

  void whereKeys(B builder, Collection<Object> key);

  void whereModel(B builder, M model);

  void whereModels(B builder, Collection<M> models);

  Map<Object, M> toKeyMap(Collection<M> models);
}
