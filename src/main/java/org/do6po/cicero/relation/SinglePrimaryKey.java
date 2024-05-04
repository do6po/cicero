package org.do6po.cicero.relation;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.do6po.cicero.model.BaseModel;
import org.do6po.cicero.query.ModelQueryBuilder;

@RequiredArgsConstructor
public class SinglePrimaryKey<M extends BaseModel<M, B>, B extends ModelQueryBuilder<M, B>>
    implements PrimaryKey<M, B> {
  private final String key;

  @Override
  public void whereKey(B builder, Object value) {
    builder.where(key, value);
  }

  @Override
  public void whereKeys(B builder, Collection<Object> values) {
    builder.whereIn(key, values);
  }

  @Override
  public void whereModel(B builder, M model) {
    whereKey(builder, extract(model));
  }

  @Override
  public void whereModels(B builder, Collection<M> models) {
    whereKeys(builder, models.stream().map(this::extract).collect(toSet()));
  }

  @Override
  public Object extract(M model) {
    return model.getAttribute(key);
  }

  @Override
  public Map<Object, M> toKeyMap(Collection<M> models) {
    return models.stream().collect(toMap(this::extract, Function.identity()));
  }
}
