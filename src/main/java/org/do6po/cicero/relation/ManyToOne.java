package org.do6po.cicero.relation;

import org.do6po.cicero.model.BaseModel;
import org.do6po.cicero.query.ModelQueryBuilder;
import org.do6po.cicero.utils.StackUtil;


public class ManyToOne<M extends BaseModel<M, ?>, F extends BaseModel<F, Q>, Q extends ModelQueryBuilder<F, Q>, R extends F> extends
    HasOne<M, F, Q, R> {

  public ManyToOne(M localModel, String localAttribute, Class<F> foreignModelClass,
      String foreignAttribute, String relationName) {
    super(localModel, localAttribute, foreignModelClass, foreignAttribute, relationName);
  }

  public ManyToOne(M localModel, String localAttribute, Class<F> foreignModelClass,
      String foreignAttribute) {
    this(localModel, localAttribute, foreignModelClass, foreignAttribute,
        StackUtil.getCallerMethodName(1));
  }
}
