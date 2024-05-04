package org.do6po.cicero.graph;

import static java.util.function.Predicate.not;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.modelmapper.internal.Pair;
import org.do6po.cicero.utils.DotUtil;

@Getter
@NoArgsConstructor
public class GraphNode {
  private final Map<String, GraphNode> graph = new HashMap<>();

  public GraphNode add(String chain) {
    Pair<String, String> result = DotUtil.cut(chain);

    String remainder = result.getRight();
    if (remainder == null || remainder.isEmpty()) {
      graph.computeIfAbsent(chain, i -> new GraphNode());
      return this;
    }

    String first = result.getLeft();
    GraphNode node = graph.getOrDefault(first, new GraphNode());
    node.add(remainder);
    graph.put(first, node);

    return this;
  }

  public GraphNode add(Set<String> chains) {
    Set<@NonNull String> clearChain =
        chains.stream().filter(not(String::isBlank)).collect(Collectors.toSet());

    clearChain.forEach(this::add);

    return this;
  }
}
