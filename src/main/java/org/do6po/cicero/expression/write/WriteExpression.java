package org.do6po.cicero.expression.write;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.do6po.cicero.expression.Expression;

public abstract class WriteExpression implements Expression {
  abstract String getTable();

  abstract List<Map<String, Object>> getValues();

  protected String columnize() {
    return String.join(", ", getColumns());
  }

  protected Set<String> getColumns() {
    return getValues().stream().findFirst().orElseThrow().keySet();
  }

  protected String columnizeUpdate() {
    return getColumns().stream().map("%s = ?"::formatted).collect(Collectors.joining(", "));
  }

  protected String parametrize() {
    return getValues().stream()
        .map(
            p ->
                "("
                    + String.join(", ", p.values().stream().map(this::getParameterBind).toList())
                    + ")")
        .collect(Collectors.joining(", "));
  }

  protected String getParameterBind(Object o) {
    return "?";
  }

  @Override
  public Collection<Object> getBindings() {
    ArrayList<Object> result = new ArrayList<>();

    for (var value : getValues()) {
      result.addAll(value.values());
    }

    return result;
  }
}
