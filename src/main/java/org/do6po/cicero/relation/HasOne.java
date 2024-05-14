package org.do6po.cicero.relation;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static org.do6po.cicero.utils.ClassUtil.getInstance;
import static org.do6po.cicero.utils.DotUtil.dot;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.do6po.cicero.exception.ExtractRelationException;
import org.do6po.cicero.model.BaseModel;
import org.do6po.cicero.query.ModelQueryBuilder;
import org.do6po.cicero.utils.StackUtil;

@RequiredArgsConstructor
public class HasOne<
        M extends BaseModel<M, ?>,
        F extends BaseModel<F, Q>,
        Q extends ModelQueryBuilder<F, Q>,
        R extends F>
    implements Relation<M, F, Q, R> {
  private final M localModel;
  private final String localAttribute;

  private final Class<F> foreignModelClass;
  private final String foreignAttribute;
  @Getter private final String relationName;

  public HasOne(
      M localModel, String localAttribute, Class<F> foreignModelClass, String foreignAttribute) {
    this(
        localModel,
        localAttribute,
        foreignModelClass,
        foreignAttribute,
        StackUtil.getCallerMethodName(1));
  }

  protected Q getForeignBuilder() {
    return getForeignModel().getBuilder();
  }

  @Override
  public Q subquery(String localTable) {
    return getForeignBuilder()
        .whereColumn(dot(getForeignTable(), foreignAttribute), dot(localTable, localAttribute));
  }

  @Override
  public Optional<F> first() {
    return whereForeign().first();
  }

  public Q whereForeign() {
    Object value = localModel.getAttribute(localAttribute);

    if (Objects.isNull(value)) {
      throw new ExtractRelationException(
          "Local attribute: '%s' not filled".formatted(localAttribute));
    }

    return getForeignBuilder().where(dot(getForeignTable(), foreignAttribute), value);
  }

  @Override
  public List<F> get() {
    return whereForeign().get();
  }

  @Override
  public R fetch() {
    return (R) first().orElseThrow();
  }

  @Override
  public List<F> load(Collection<M> models) {
    Set<Object> keys = models.stream().map(i -> i.getAttribute(localAttribute)).collect(toSet());
    List<F> foreignModels =
        getForeignBuilder().whereIn(dot(getForeignTable(), foreignAttribute), keys).get();

    Map<Object, F> map =
        foreignModels.stream().collect(toMap(i -> i.getAttribute(foreignAttribute), identity()));

    for (M model : models) {
      model.setRelation(getRelationName(), map.get(model.getAttribute(localAttribute)));
    }

    return foreignModels;
  }

  protected F getForeignModel() {
    return getInstance(foreignModelClass);
  }

  protected String getForeignTable() {
    return getForeignModel().getTable();
  }
}
