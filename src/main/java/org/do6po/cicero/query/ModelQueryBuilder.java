package org.do6po.cicero.query;

import static org.do6po.cicero.utils.ClassUtil.getInstance;
import static org.do6po.cicero.utils.DotUtil.dot;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.do6po.cicero.enums.OperatorEnum;
import org.do6po.cicero.enums.PredicateOperatorEnum;
import org.do6po.cicero.filter.ModelFilter;
import org.do6po.cicero.iterator.ChunkIterator;
import org.do6po.cicero.iterator.ModelChunkIterator;
import org.do6po.cicero.model.BaseModel;
import org.do6po.cicero.relation.Relation;
import org.do6po.cicero.relation.RelationLoader;
import org.do6po.cicero.relation.RelationLoaderBuffer;
import org.do6po.cicero.utils.ClassUtil;
import org.do6po.cicero.utils.RowMapper;
import org.do6po.cicero.utils.StringUtil;

@Getter
public abstract class ModelQueryBuilder<
        M extends BaseModel<M, B>, B extends ModelQueryBuilder<M, B>>
    extends Builder<M, B> implements RelationLoaderBuffer<M, B> {

  protected final Class<M> modelClass;
  protected final M model;

  protected Set<String> with = new HashSet<>();
  protected boolean nullifyRelations = false;

  protected ModelQueryBuilder(Class<M> modelClass) {
    this.modelClass = modelClass;
    model = getInstance(modelClass);

    init();
  }

  public static <M extends BaseModel<M, B>, B extends ModelQueryBuilder<M, B>> B query(
      Class<M> modelClass) {
    return ClassUtil.getInstance(modelClass).getBuilder();
  }

  protected void init() {
    connection = model.getConnection();
    from(model.getTable()).select("*");
  }

  @SneakyThrows
  @Override
  public M mapItem(ResultSet resultSet) {
    M newInstance = getInstance(modelClass);

    newInstance.setAttributes(RowMapper.rowToMap(resultSet, newInstance.getTable()));

    return newInstance;
  }

  public B whereKey(Object identify) {
    getModel().getModelKey().whereKey(self(), identify);

    return self();
  }

  public B whereKey(Collection<Object> identify) {
    getModel().getModelKey().whereKeys(self(), identify);

    return self();
  }

  public B nullifyRelations() {
    nullifyRelations = true;

    return self();
  }

  public B withoutNullifyRelations() {
    nullifyRelations = false;

    return self();
  }

  @Override
  public B with(Collection<String> relations) {
    with.addAll(relations);

    return self();
  }

  @Override
  public B with(String... relations) {
    return with(Arrays.asList(relations));
  }

  @Override
  public <F extends BaseModel<F, Q>, Q extends ModelQueryBuilder<F, Q>, R> B with(
      Function<M, Relation<M, F, Q, R>> extractor) {
    return with(extractor, UnaryOperator.identity());
  }

  @Override
  public <F extends BaseModel<F, Q>, Q extends ModelQueryBuilder<F, Q>, R> B with(
      Function<M, Relation<M, F, Q, R>> extractor,
      UnaryOperator<RelationLoaderBuffer<F, Q>> query) {

    Relation<M, F, Q, R> relation = extractor.apply(getModel());
    Set<String> foreignWith = query.apply(relation.subquery(getModel().getTable())).getWith();
    String relationToForeign = relation.getRelationName();

    if (foreignWith.isEmpty()) {
      return with(relationToForeign);
    }

    return with(foreignWith.stream().map(r -> dot(relationToForeign, r)).toList());
  }

  public <F extends BaseModel<F, Q>, Q extends ModelQueryBuilder<F, Q>, R> B has(
      Function<M, Relation<M, F, Q, R>> extractor, OperatorEnum type) {
    return whereHas(extractor, UnaryOperator.identity(), type);
  }

  public <F extends BaseModel<F, Q>, Q extends ModelQueryBuilder<F, Q>, R> B has(
      Function<M, Relation<M, F, Q, R>> extractor) {
    return has(extractor, OperatorEnum.AND);
  }

  public <F extends BaseModel<F, Q>, Q extends ModelQueryBuilder<F, Q>, R> B has(
      Function<M, Relation<M, F, Q, R>> extractor, PredicateOperatorEnum operator, int value) {
    return has(extractor, operator.getValue(), value, OperatorEnum.AND);
  }

  public <F extends BaseModel<F, Q>, Q extends ModelQueryBuilder<F, Q>, R> B has(
      Function<M, Relation<M, F, Q, R>> extractor,
      PredicateOperatorEnum operator,
      int value,
      OperatorEnum type) {
    return has(extractor, operator.getValue(), value, type);
  }

  public <F extends BaseModel<F, Q>, Q extends ModelQueryBuilder<F, Q>, R> B has(
      Function<M, Relation<M, F, Q, R>> extractor, String operator, int value, OperatorEnum type) {
    return whereHas(extractor, UnaryOperator.identity(), operator, value, type);
  }

  public <F extends BaseModel<F, Q>, Q extends ModelQueryBuilder<F, Q>, R> B whereHas(
      Function<M, Relation<M, F, Q, R>> extractor, UnaryOperator<Q> query, OperatorEnum type) {
    Relation<M, F, Q, R> relation = extractor.apply(getModel());
    Q modelQueryBuilder = relation.subquery(getModel().getTable());
    return exists(query.apply(modelQueryBuilder), type);
  }

  public <F extends BaseModel<F, Q>, Q extends ModelQueryBuilder<F, Q>, R> B whereHas(
      Function<M, Relation<M, F, Q, R>> extractor, UnaryOperator<Q> query) {
    return whereHas(extractor, query, OperatorEnum.AND);
  }

  public <F extends BaseModel<F, Q>, Q extends ModelQueryBuilder<F, Q>, R> B whereHas(
      Function<M, Relation<M, F, Q, R>> extractor,
      UnaryOperator<Q> query,
      String operator,
      int value,
      OperatorEnum type) {
    return whereHas(
        extractor, b -> query.apply(b.select("count(*)")), operator, (Object) value, type);
  }

  public <F extends BaseModel<F, Q>, Q extends ModelQueryBuilder<F, Q>, R> B whereHas(
      Function<M, Relation<M, F, Q, R>> extractor,
      UnaryOperator<Q> query,
      String operator,
      int value) {
    return whereHas(extractor, query, operator, value, OperatorEnum.AND);
  }

  public <F extends BaseModel<F, Q>, Q extends ModelQueryBuilder<F, Q>, R> B whereHas(
      Function<M, Relation<M, F, Q, R>> extractor,
      UnaryOperator<Q> query,
      String operator,
      Object value,
      OperatorEnum type) {
    Relation<M, F, Q, R> relation = extractor.apply(getModel());
    Q modelQueryBuilder = relation.subquery(getModel().getTable());
    return where(query.apply(modelQueryBuilder), operator, value, type);
  }

  public <F extends BaseModel<F, Q>, Q extends ModelQueryBuilder<F, Q>, R> B whereHas(
      Function<M, Relation<M, F, Q, R>> extractor,
      UnaryOperator<Q> query,
      String operator,
      Object value) {
    return whereHas(extractor, query, operator, value, OperatorEnum.AND);
  }

  public <F extends BaseModel<F, Q>, Q extends ModelQueryBuilder<F, Q>, R> B whereHas(
      Function<M, Relation<M, F, Q, R>> extractor,
      UnaryOperator<Q> query,
      PredicateOperatorEnum operator,
      int value) {
    return whereHas(extractor, query, operator.getValue(), value);
  }

  public <F extends BaseModel<F, Q>, Q extends ModelQueryBuilder<F, Q>, R> B withCount(
      Function<M, Relation<M, F, Q, R>> extractor, UnaryOperator<Q> q, String as) {
    Relation<M, F, Q, R> relation = extractor.apply(getModel());
    Q modelQueryBuilder = relation.subquery(getModel().getTable());
    return addSelect(q.apply(modelQueryBuilder).select("count(*)").one(), as);
  }

  public <F extends BaseModel<F, Q>, Q extends ModelQueryBuilder<F, Q>, R> B withCount(
      Function<M, Relation<M, F, Q, R>> extractor, UnaryOperator<Q> q) {
    Relation<M, F, Q, R> relation = extractor.apply(getModel());
    String as = StringUtil.camelCaseToSnakeCase(relation.getRelationName()) + "_" + COUNT_ALIAS;
    return withCount(extractor, q, as);
  }

  public <F extends BaseModel<F, Q>, Q extends ModelQueryBuilder<F, Q>, R> B withCount(
      Function<M, Relation<M, F, Q, R>> extractor, String as) {
    return withCount(extractor, UnaryOperator.identity(), as);
  }

  public <F extends BaseModel<F, Q>, Q extends ModelQueryBuilder<F, Q>, R> B withCount(
      Function<M, Relation<M, F, Q, R>> extractor) {
    return withCount(extractor, UnaryOperator.identity());
  }

  public B filter(ModelFilter<M, B> filter) {
    filter.fill(self());

    return self();
  }

  public Optional<M> find(Object identify) {
    return whereKey(identify).first();
  }

  @NonNull
  public M findOrFail(Object identify) {
    return whereKey(identify).first().orElseThrow();
  }

  public List<M> find(Collection<Object> identify) {
    return whereKey(identify).get();
  }

  @Override
  public List<M> get() {
    List<M> result = super.get();

    new RelationLoader().nullifyEmptyRelations(nullifyRelations).load(result, with);

    return result;
  }

  public <L extends List<M>> L get(Function<List<M>, L> function) {
    return function.apply(get());
  }

  @Override
  public ChunkIterator<M, B> chunk(int chunk) throws SQLException {
    return new ModelChunkIterator<>(self(), chunk);
  }
}
