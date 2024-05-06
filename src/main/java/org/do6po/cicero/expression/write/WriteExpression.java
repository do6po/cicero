package org.do6po.cicero.expression.write;

import org.do6po.cicero.expression.Expression;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public interface WriteExpression extends Expression {
  String getTable();

  List<Map<String, Object>> getValues();

  default String getInsertStatement(String insertPattern) {
    String columns = columnize(getValues().stream().findFirst().orElseThrow().keySet());
    String parameters =
        getValues().stream()
            .map(p -> "(" + parametrize(p.values()) + ")")
            .collect(Collectors.joining(", "));

    return insertPattern.formatted(getTable(), columns, parameters);
  }

  default String columnize(Set<String> keySet) {
    return String.join(", ", keySet);
  }

  default String parametrize(Collection<Object> parameters) {
    return String.join(", ", parameters.stream().map(this::getParameterBind).toList());
  }

  default String getParameterBind(Object o) {
    return "?";
  }

  @Override
  default Collection<Object> getBindings() {
    ArrayList<Object> result = new ArrayList<>();

    for (var value : getValues()) {
      result.addAll(value.values());
    }

    return result;
  }
}
