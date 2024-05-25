package org.do6po.cicero.relation;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.do6po.cicero.graph.GraphNode;
import org.do6po.cicero.model.BaseModel;
import org.do6po.cicero.query.ModelQueryBuilder;
import org.do6po.cicero.utils.RelationUtil;

@Setter
@Accessors(fluent = true, chain = true)
public class RelationLoader {

  private boolean nullifyEmptyRelations = false;

  public <M extends BaseModel<M, B>, B extends ModelQueryBuilder<M, B>> void load(
      @NonNull Collection<M> models, @NonNull Set<String> chains) {
    if (models.isEmpty()) {
      return;
    }

    load(models, new GraphNode().add(chains));
  }

  public <M extends BaseModel<M, B>, B extends ModelQueryBuilder<M, B>> void load(
      @NonNull Collection<M> models, @NonNull GraphNode graphNode) {
    Map<String, GraphNode> graph = graphNode.getGraph();

    if (nullifyEmptyRelations) {
      nullifyRelations(models);
    }

    if (graph.isEmpty()) {
      return;
    }

    M currentModel = models.stream().findFirst().orElseThrow();

    for (Entry<String, GraphNode> branch : graph.entrySet()) {
      load(models, branch, currentModel);
    }
  }

  private <M extends BaseModel<M, B>, B extends ModelQueryBuilder<M, B>> void nullifyRelations(
      Collection<M> models) {
    M currentModel = models.stream().findFirst().orElseThrow();
    Collection<String> relations = RelationUtil.extractRelationMethodNames(currentModel);

    for (M model : models) {
      for (String relation : relations) {
        if (!model.hasRelation(relation)) {
          model.setRelation(relation, null);
        }
      }
    }
  }

  public <
          M extends BaseModel<M, ?>,
          F extends BaseModel<F, Q>,
          Q extends ModelQueryBuilder<F, Q>,
          R>
      void load(Collection<M> models, Entry<String, GraphNode> branch, M currentModel) {
    Relation<M, F, Q, R> relation = RelationUtil.extractRelation(currentModel, branch.getKey());
    List<F> foreignModels = relation.load(models);

    load(foreignModels, branch.getValue());
  }
}
