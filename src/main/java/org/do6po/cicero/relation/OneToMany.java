package org.do6po.cicero.relation;

import static java.util.stream.Collectors.toSet;
import static org.do6po.cicero.utils.ClassUtil.getInstance;
import static org.do6po.cicero.utils.DotUtil.dot;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.do6po.cicero.exception.ExtractRelationException;
import org.do6po.cicero.model.BaseModel;
import org.do6po.cicero.query.ModelQueryBuilder;
import org.do6po.cicero.utils.StackUtil;

@RequiredArgsConstructor
public class OneToMany<
        M extends BaseModel<M, ?>,
        F extends BaseModel<F, Q>,
        Q extends ModelQueryBuilder<F, Q>,
        R extends List<F>>
    implements Relation<M, F, Q, R> {
  private final M localModel;
  private final String localAttribute;

  private final Class<F> foreignModelClass;
  private final String foreignAttribute;

  @Getter private final String relationName;

  public OneToMany(
      M localModel, String localAttribute, Class<F> foreignClass, String foreignAttribute) {
    this(
        localModel,
        localAttribute,
        foreignClass,
        foreignAttribute,
        StackUtil.getCallerMethodName(1));
  }

  protected Q getForeignModelBuilder() {
    return getForeignModel().getBuilder();
  }

  @Override
  public Q subquery(String localTable) {
    return getForeignModelBuilder()
        .whereColumn(
            dot(getLocalTable(), localAttribute), dot(getForeignTable(), foreignAttribute));
  }

  public Q whereForeign() {
    Object value = localModel.getAttribute(localAttribute);

    if (Objects.isNull(value)) {
      throw new ExtractRelationException(
          "Local attribute: '%s' not filled".formatted(localAttribute));
    }

    return getForeignModelBuilder().where(dot(getForeignTable(), foreignAttribute), value);
  }

  @Override
  public Optional<F> first() {
    return whereForeign().first();
  }

  @Override
  public List<F> get() {
    return whereForeign().get();
  }

  @Override
  public R fetch() {
    return (R) get();
  }

  @Override
  public List<F> load(Collection<M> models) {
    Set<Object> keys = models.stream().map(i -> i.getAttribute(localAttribute)).collect(toSet());

    List<F> foreignModels =
        getForeignModelBuilder().whereIn(dot(getForeignTable(), foreignAttribute), keys).get();

    Map<Object, List<F>> map =
        foreignModels.stream()
            .collect(Collectors.groupingBy(i -> i.getAttribute(foreignAttribute)));

    for (M model : models) {
      model.setRelation(getRelationName(), map.get(model.getAttribute(localAttribute)));
    }

    return foreignModels;
  }

  protected String getLocalTable() {
    return localModel.getTable();
  }

  protected String getForeignTable() {
    return getForeignModel().getTable();
  }

  protected F getForeignModel() {
    return getInstance(foreignModelClass);
  }
}
