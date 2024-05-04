package org.do6po.cicero.test.unit.graph;

import static org.assertj.core.api.Assertions.assertThat;
import static org.do6po.cicero.utils.DotUtil.dot;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.do6po.cicero.graph.GraphNode;
import org.junit.jupiter.api.Test;

class GraphNodeTest {

  @Test
  void transformChainWithOnRelations() {
    Set<String> chain = Set.of("relation");

    GraphNode graphNode = new GraphNode().add(chain);

    Set<Entry<String, GraphNode>> actual = graphNode.getGraph().entrySet();
    assertThat(actual).hasSize(1);

    assertThat(actual.stream().findFirst().orElseThrow().getValue())
        .isEqualToComparingFieldByField(new GraphNode());
  }

  @Test
  void transformChainWithTwoRelations() {
    String rel1 = "relation1";
    String rel2 = "relation2";

    Set<String> chain = Set.of(dot(rel1, rel2));
    GraphNode graphNode = new GraphNode().add(chain);

    GraphNode subGraph = graphNode.getGraph().get(rel1);
    assertTrue(subGraph.getGraph().get(rel2).getGraph().isEmpty());
  }

  @Test
  void transformChainWithListOfRelations() {
    String rel1 = "relation1";
    String rel2 = "relation2";
    String rel3 = "relation3";
    String rel4 = "relation4";

    Set<String> chain = Set.of(dot(rel1, rel4), dot(rel1, rel2), dot(rel1, rel2, rel3));
    GraphNode graphNode = new GraphNode().add(chain);

    Map<String, GraphNode> graph = graphNode.getGraph();

    assertThat(graph).hasSize(1);

    Map<String, GraphNode> graph1 = graph.get(rel1).getGraph();
    assertThat(graph1).hasSize(2);
    assertThat(graph1.get(rel4).getGraph()).isEmpty();

    Map<String, GraphNode> graph2 = graph1.get(rel2).getGraph();
    assertThat(graph2).hasSize(1);

    Map<String, GraphNode> graph3 = graph2.get(rel3).getGraph();
    assertThat(graph3).isEmpty();
  }
}
