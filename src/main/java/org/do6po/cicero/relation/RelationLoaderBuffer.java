package org.do6po.cicero.relation;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import org.do6po.cicero.model.BaseModel;
import org.do6po.cicero.query.ModelQueryBuilder;

public interface RelationLoaderBuffer<
    M extends BaseModel<M, B>, B extends ModelQueryBuilder<M, B>> {

  Set<String> getWith();

  B with(Collection<String> relations);

  B with(String... relations);

  B withNullify();

  B withoutNullify();

  <F extends BaseModel<F, Q>, Q extends ModelQueryBuilder<F, Q>, R> B with(
      Function<M, Relation<M, F, Q, R>> extractor);

  <F extends BaseModel<F, Q>, Q extends ModelQueryBuilder<F, Q>, R> B with(
      Function<M, Relation<M, F, Q, R>> extractor, UnaryOperator<RelationLoaderBuffer<F, Q>> query);
}
