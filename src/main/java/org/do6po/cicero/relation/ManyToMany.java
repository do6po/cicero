package org.do6po.cicero.relation;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static org.do6po.cicero.utils.ClassUtil.getInstance;
import static org.do6po.cicero.utils.DotUtil.dot;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.do6po.cicero.model.BaseModel;
import org.do6po.cicero.query.ModelQueryBuilder;
import org.do6po.cicero.query.QueryBuilder;
import org.do6po.cicero.utils.ClassUtil;
import org.do6po.cicero.utils.StackUtil;

@RequiredArgsConstructor
public class ManyToMany<
        M extends BaseModel<M, ?>,
        F extends BaseModel<F, Q>,
        Q extends ModelQueryBuilder<F, Q>,
        R extends List<F>>
    implements Relation<M, F, Q, R> {

  private final M localModel;
  private final String localAttribute;
  private final String pivotTable;
  private final String pivotLocalAttribute;
  private final String pivotForeignAttribute;
  private final String foreignAttribute;
  private final Class<F> foreignModelClass;
  @Getter private final String relationName;

  public ManyToMany(
      M localModel,
      String localAttribute,
      String pivotTable,
      String pivotLocalAttribute,
      String pivotForeignAttribute,
      String foreignAttribute,
      Class<F> foreignModelClass) {
    this.localModel = localModel;
    this.localAttribute = localAttribute;
    this.pivotTable = pivotTable;
    this.pivotLocalAttribute = pivotLocalAttribute;
    this.pivotForeignAttribute = pivotForeignAttribute;
    this.foreignAttribute = foreignAttribute;
    this.foreignModelClass = foreignModelClass;
    this.relationName = StackUtil.getCallerMethodName(1);
  }

  protected Q getForeignModelBuilder() {
    return getForeignModel().getBuilder();
  }

  protected F getForeignModel() {
    return getInstance(foreignModelClass);
  }

  @Override
  public Q subquery(String localTable) {
    return getForeignModelBuilder()
        .whereIn(
            foreignAttribute,
            QueryBuilder.query(pivotTable)
                .select(pivotForeignAttribute)
                .distinct(true)
                .whereColumn(
                    dot(pivotTable, pivotLocalAttribute), dot(localTable, localAttribute)));
  }

  @Override
  public Optional<F> first() {
    return findByLocalKeys(Set.of(getLocalAttributeValue())).one().first();
  }

  protected Q findByLocalKeys(Collection<Object> localKeys) {
    return getForeignModelBuilder()
        .whereIn(
            foreignAttribute,
            QueryBuilder.query(pivotTable)
                .select(pivotForeignAttribute)
                .distinct(true)
                .whereIn(dot(pivotTable, pivotLocalAttribute), localKeys));
  }

  @Override
  public List<F> get() {
    return findByLocalKeys(Set.of(getLocalAttributeValue())).get();
  }

  @Override
  public R fetch() {
    return (R) get();
  }

  @Override
  public List<F> load(Collection<M> models) {
    F foreignModel = ClassUtil.getInstance(foreignModelClass);

    Set<Object> keys = models.stream().map(i -> i.getAttribute(localAttribute)).collect(toSet());

    List<F> foreignModels =
        getForeignModelBuilder()
            .innerJoin(
                pivotTable,
                dot(pivotTable, pivotForeignAttribute),
                dot(foreignModel.getTable(), foreignAttribute))
            .whereIn(dot(pivotTable, pivotLocalAttribute), keys)
            .get();

    Map<Object, Set<Object>> groupByLocalKey =
        foreignModels.stream()
            .collect(
                groupingBy(
                    f -> f.getAttribute(dot(pivotTable, pivotLocalAttribute)),
                    mapping(i -> i.getAttribute(dot(pivotTable, pivotForeignAttribute)), toSet())));

    Map<Object, F> foreignModelsMap =
        foreignModels.stream()
            .collect(toMap(i -> i.getAttribute(foreignAttribute), identity(), (v1, v2) -> v1));

    for (M model : models) {
      model.setRelation(
          getRelationName(),
          groupByLocalKey.getOrDefault(model.getAttribute(localAttribute), new HashSet<>()).stream()
              .map(foreignModelsMap::get)
              .collect(toList()));
    }

    return foreignModels;
  }

  protected Object getLocalAttributeValue() {
    return localModel.getAttribute(localAttribute);
  }
}
