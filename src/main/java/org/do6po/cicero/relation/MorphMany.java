package org.do6po.cicero.relation;

import static java.util.stream.Collectors.toSet;
import static org.do6po.cicero.utils.DotUtil.dot;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.do6po.cicero.model.BaseModel;
import org.do6po.cicero.query.ModelQueryBuilder;
import org.do6po.cicero.utils.ClassUtil;
import org.do6po.cicero.utils.StackUtil;

@Getter
@RequiredArgsConstructor
public class MorphMany<
        M extends BaseModel<M, ?>,
        F extends BaseModel<F, Q>,
        Q extends ModelQueryBuilder<F, Q>,
        R extends List<F>>
    implements Relation<M, F, Q, R> {

  protected final M localModel;
  protected final String localAttribute;

  protected final Class<F> foreignModelClass;
  protected final String foreignReferenceTypeAttribute;
  protected final String foreignReferenceIdAttribute;

  protected final Object foreignReferenceTypeValue;
  protected final String relationName;

  public MorphMany(
      M localModel,
      String localAttribute,
      Class<F> foreignModelClass,
      String foreignReferenceTypeAttribute,
      String foreignReferenceIdAttribute,
      Object foreignReferenceTypeValue) {
    this(
        localModel,
        localAttribute,
        foreignModelClass,
        foreignReferenceTypeAttribute,
        foreignReferenceIdAttribute,
        foreignReferenceTypeValue,
        StackUtil.getCallerMethodName(1));
  }

  public MorphMany(
      M localModel,
      String localAttribute,
      Class<F> foreignModelClass,
      String foreignReferenceTypeAttribute,
      String foreignReferenceIdAttribute) {
    this(
        localModel,
        localAttribute,
        foreignModelClass,
        foreignReferenceTypeAttribute,
        foreignReferenceIdAttribute,
        localModel.getClass());
  }

  protected Q getForeignBuilder() {
    return getForeignModel().getBuilder();
  }

  private F getForeignModel() {
    return ClassUtil.getInstance(foreignModelClass);
  }

  @Override
  public Q subquery(String localTable) {
    String foreignTable = getForeignTable();

    return getForeignBuilder()
        .where(dot(foreignTable, getForeignReferenceTypeAttribute()), foreignReferenceTypeValue)
        .whereColumn(
            dot(foreignTable, getForeignReferenceIdAttribute()), dot(localTable, localAttribute));
  }

  @Override
  public Optional<F> first() {
    return whereForeignCriteria(Set.of(getLocalAttributeValue())).first();
  }

  @Override
  public List<F> get() {
    return whereForeignCriteria(Set.of(getLocalAttributeValue())).get();
  }

  protected Object getLocalAttributeValue() {
    return localModel.getAttribute(localAttribute);
  }

  protected Q whereForeignCriteria(Set<Object> referenceIds) {
    String foreignTable = getForeignTable();

    return getForeignBuilder()
        .where(dot(foreignTable, getForeignReferenceTypeAttribute()), foreignReferenceTypeValue)
        .whereIn(dot(foreignTable, getForeignReferenceIdAttribute()), referenceIds);
  }

  @Override
  public R fetch() {
    return (R) get();
  }

  @Override
  public List<F> load(Collection<M> models) {
    Set<Object> keys = models.stream().map(i -> i.getAttribute(localAttribute)).collect(toSet());
    List<F> foreignModels = whereForeignCriteria(keys).get();

    Map<Object, List<F>> map =
        foreignModels.stream()
            .collect(Collectors.groupingBy(i -> i.getAttribute(foreignReferenceIdAttribute)));

    for (M model : models) {
      model.setRelation(getRelationName(), map.get(model.getAttribute(localAttribute)));
    }

    return foreignModels;
  }

  private String getForeignTable() {
    return getForeignModel().getTable();
  }
}
