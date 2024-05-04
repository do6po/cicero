package org.do6po.cicero.collection;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;
import org.do6po.cicero.model.BaseModel;
import org.do6po.cicero.query.ModelQueryBuilder;
import org.do6po.cicero.utils.RelationUtil;

public class ModelList<M extends BaseModel<M, B>, B extends ModelQueryBuilder<M, B>>
    extends ArrayList<M> {

  public ModelList(List<M> c) {
    super(c);
  }

  public ModelList(Stream<M> c) {
    this(c.collect(toList()));
  }

  public Optional<M> first() {
    return this.stream().findFirst();
  }

  public M firstOrThrow() {
    return first().orElseThrow();
  }

  public Map<Object, M> toMapByKey() {
    return toMapByKey(identity());
  }

  public <U> Map<Object, U> toMapByKey(Function<? super M, ? extends U> extractor) {
    M m = firstOrThrow();

    return this.stream().collect(toMap(i -> i.getAttribute(m.getPrimaryKey()), extractor));
  }

  public M whereKey(Object key) {
    return toMapByKey().get(key);
  }

  public ModelList<M, B> whereKey(Set<Object> keys) {
    Map<Object, M> mappedByKey = toMapByKey();
    return new ModelList<>(keys.stream().map(mappedByKey::get));
  }

  public ModelList<M, B> load(Set<String> relations) {
    RelationUtil.load(this, relations);

    return this;
  }
}
