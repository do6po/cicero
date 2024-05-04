package org.do6po.cicero.relation;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.do6po.cicero.model.BaseModel;
import org.do6po.cicero.query.ModelQueryBuilder;

public interface Relation<
    M extends BaseModel<M, ?>, F extends BaseModel<F, Q>, Q extends ModelQueryBuilder<F, Q>, R> {

  String getRelationName();

  Q subquery(String localTable);

  Optional<F> first();

  List<F> get();

  R fetch();

  List<F> load(Collection<M> models);
}
