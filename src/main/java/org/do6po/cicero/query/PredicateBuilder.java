package org.do6po.cicero.query;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.do6po.cicero.expression.predicate.PredicateExpression;

@Getter
public class PredicateBuilder implements CriteriaBuilder<PredicateBuilder> {
  private final List<PredicateExpression> predicates = new ArrayList<>();
}
