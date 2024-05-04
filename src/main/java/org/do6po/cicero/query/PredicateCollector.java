package org.do6po.cicero.query;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import org.do6po.cicero.enums.OperatorEnum;
import org.do6po.cicero.expression.predicate.PredicateExpression;
import org.do6po.cicero.expression.predicate.StringPredicateExpression;

public class PredicateCollector {

  public PredicateExpression collect(List<PredicateExpression> predicates, OperatorEnum type) {
    StringBuilder longExpression = new StringBuilder();
    ArrayList<Object> bindingResult = new ArrayList<>();

    ListIterator<PredicateExpression> iterator = predicates.listIterator();
    while (iterator.hasNext()) {
      boolean hasPrevious = iterator.hasPrevious();

      PredicateExpression predicate = iterator.next();

      if (hasPrevious) {
        longExpression.append(" %s ".formatted(predicate.getType().getValue()));
      }

      longExpression.append(predicate.getExpression());
      bindingResult.addAll(predicate.getBindings());
    }

    return new StringPredicateExpression(longExpression.toString(), bindingResult, type);
  }
}
