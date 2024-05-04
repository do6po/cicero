package org.do6po.cicero.model;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.do6po.cicero.query.ModelQueryBuilder;
import org.do6po.cicero.relation.PrimaryKey;
import org.do6po.cicero.relation.Relation;
import org.do6po.cicero.relation.SinglePrimaryKey;
import org.do6po.cicero.utils.ClassUtil;
import org.do6po.cicero.utils.RelationUtil;

public abstract class BaseModel<M extends BaseModel<M, B>, B extends ModelQueryBuilder<M, B>> {
  @Getter protected PrimaryKey<M, B> modelKey = new SinglePrimaryKey<>("id");
  @Getter protected String primaryKey = "id";
  @Getter protected String connection = "default";
  @Getter protected boolean incrementing = true;
  @Setter protected Map<String, Object> attributes = new HashMap<>();

  protected Map<String, Object> original = new HashMap<>();
  protected Map<String, Object> changes = new HashMap<>();
  protected Map<String, Object> relations = new HashMap<>();

  public B getBuilder() {
    Class<B> buildrCLass = ClassUtil.guessType(getClass(), 1);

    return ClassUtil.getInstance(buildrCLass);
  }

  public abstract String getTable();

  public boolean hasAttribute(String key) {
    return attributes.containsKey(key);
  }

  @SuppressWarnings("unchecked")
  public <T> T getAttribute(@NonNull String key) {
    return (T) attributes.get(key);
  }

  @SuppressWarnings("unchecked")
  public <R> R getRelation(@NonNull String key) {
    if (relations.containsKey(key)) {
      return (R) relations.get(key);
    }

    return (R) setRelation(key, RelationUtil.extractRelation(this, key).fetch());
  }

  public boolean hasRelation(@NonNull String key) {
    return relations.containsKey(key);
  }

  @SuppressWarnings("unchecked")
  public <F extends BaseModel<F, Q>, Q extends ModelQueryBuilder<F, Q>, R> R getRelation(
      Function<M, Relation<M, F, Q, R>> extractor) {
    Relation<M, F, Q, R> relation = extractor.apply((M) this);
    String key = relation.getRelationName();

    if (relations.containsKey(key)) {
      return (R) relations.get(key);
    }

    return setRelation(key, relation.fetch());
  }

  public <R> R setRelation(@NonNull String relation, R object) {
    relations.put(relation, object);

    return object;
  }
}
