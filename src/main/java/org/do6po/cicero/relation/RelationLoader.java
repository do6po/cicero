package org.do6po.cicero.relation;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import lombok.NonNull;
import lombok.Setter;
import org.do6po.cicero.graph.GraphNode;
import org.do6po.cicero.model.BaseModel;
import org.do6po.cicero.query.ModelQueryBuilder;
import org.do6po.cicero.utils.RelationUtil;

@Setter
public class RelationLoader {

  private boolean nullify = false;

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

    if (graph.isEmpty()) {
      return;
    }

    M currentModel = models.stream().findFirst().orElseThrow();

    for (Entry<String, GraphNode> branch : graph.entrySet()) {
      load(models, branch, currentModel);
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
