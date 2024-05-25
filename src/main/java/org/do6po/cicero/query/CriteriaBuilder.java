package org.do6po.cicero.query;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Consumer;
import org.do6po.cicero.enums.OperatorEnum;
import org.do6po.cicero.enums.PredicateOperatorEnum;
import org.do6po.cicero.expression.predicate.CalculatedPredicateExpression;
import org.do6po.cicero.expression.predicate.ColumnPredicateExpression;
import org.do6po.cicero.expression.predicate.NestedPredicateExpression;
import org.do6po.cicero.expression.predicate.NullPredicateExpression;
import org.do6po.cicero.expression.predicate.PredicateExpression;
import org.do6po.cicero.expression.predicate.SimplePredicateExpression;
import org.do6po.cicero.expression.predicate.StringPredicateExpression;
import org.do6po.cicero.expression.subquery.ExistsPredicateExpression;
import org.do6po.cicero.expression.subquery.QueryExpression;
import org.do6po.cicero.expression.subquery.WhereInPredicateExpression;

public interface CriteriaBuilder<B extends CriteriaBuilder<B>> {

  PredicateCollector COLLECTOR = new PredicateCollector();

  List<PredicateExpression> getPredicates();

  default B self() {
    return (B) this;
  }

  default B where(PredicateExpression predicate) {
    getPredicates().add(predicate);

    return self();
  }

  default B where(Consumer<CriteriaBuilder<?>> function) {
    return where(function, OperatorEnum.AND);
  }

  default B whereAre(LinkedHashMap<String, Object> predicates) {
    predicates.forEach(this::where);

    return self();
  }

  default B whereAre(Consumer<AttributeHolder> consumer) {
    AttributeHolder attributes = new AttributeHolder();

    consumer.accept(attributes);

    whereAre(attributes.getAttributes());

    return self();
  }

  default B orWhereAre(Consumer<AttributeHolder> consumer) {
    AttributeHolder attributes = new AttributeHolder();

    consumer.accept(attributes);

    orWhereAre(attributes.getAttributes());

    return self();
  }

  default B orWhereAre(LinkedHashMap<String, Object> predicates) {
    predicates.forEach(this::orWhere);

    return self();
  }

  default B orWhere(Consumer<CriteriaBuilder<?>> function) {
    return where(function, OperatorEnum.OR);
  }

  default B where(Consumer<CriteriaBuilder<?>> function, OperatorEnum type) {
    CriteriaBuilder<PredicateBuilder> criteriaBuilder = new PredicateBuilder();
    function.accept(criteriaBuilder);
    return where(new NestedPredicateExpression(criteriaBuilder.getPredicateExpression(type), type));
  }

  default B where(String column, Object value) {
    return where(column, PredicateOperatorEnum.EQUALS.getValue(), value);
  }

  default B where(String column, String operator, Object value) {
    return where(new SimplePredicateExpression(column, operator, value));
  }

  default B orWhere(String column, Object value) {
    return orWhere(column, PredicateOperatorEnum.EQUALS.getValue(), value);
  }

  default B orWhere(String column, String operator, Object value) {
    return where(new SimplePredicateExpression(column, operator, value, OperatorEnum.OR));
  }

  default B where(String column, PredicateOperatorEnum operator, Object value) {
    return where(column, operator.getValue(), value);
  }

  default B isNull(String column, OperatorEnum type) {
    return where(new NullPredicateExpression(column, true, type));
  }

  default B isNull(String column) {
    return isNull(column, OperatorEnum.AND);
  }

  default B isNotNull(String column, OperatorEnum type) {
    return where(new NullPredicateExpression(column, false, type));
  }

  default B isNotNull(String column) {
    return isNotNull(column, OperatorEnum.AND);
  }

  default B whereRaw(String raw, Collection<Object> bindings, OperatorEnum type) {
    return where(new StringPredicateExpression(raw, bindings, type));
  }

  default B whereRaw(String raw, Collection<Object> bindings) {
    return whereRaw(raw, bindings, OperatorEnum.AND);
  }

  default B orWhereRaw(String raw, Collection<Object> bindings) {
    return whereRaw(raw, bindings, OperatorEnum.OR);
  }

  default B whereIn(String column, List<String> values) {
    return whereIn(column, values.stream().map(i -> (Object) i).toList());
  }

  default B whereIn(String column, Collection<Object> values) {
    return where(
        new SimplePredicateExpression(column, PredicateOperatorEnum.IN.getValue(), values));
  }

  default B whereColumn(String first, String operator, String second, OperatorEnum type) {
    return where(new ColumnPredicateExpression(first, operator, second, type));
  }

  default B whereColumn(
      String first, PredicateOperatorEnum operator, String second, OperatorEnum type) {
    return whereColumn(first, operator.getValue(), second, type);
  }

  default B whereColumn(String first, String second) {
    return whereColumn(first, PredicateOperatorEnum.EQUALS, second, OperatorEnum.AND);
  }

  default B orWhereColumn(String first, String second) {
    return whereColumn(first, PredicateOperatorEnum.EQUALS, second, OperatorEnum.OR);
  }

  default B whereIn(String column, QueryExpression expression, boolean not, OperatorEnum type) {
    return where(new WhereInPredicateExpression(column, expression, not, type));
  }

  default B whereIn(String column, QueryExpression expression) {
    return whereIn(column, expression, false, OperatorEnum.AND);
  }

  default B whereNotIn(String column, QueryExpression expression, OperatorEnum type) {
    return whereIn(column, expression, true, type);
  }

  default B whereNotIn(String column, QueryExpression expression) {
    return whereNotIn(column, expression, OperatorEnum.AND);
  }

  default B orWhereIn(String column, QueryExpression expression, boolean not) {
    return whereIn(column, expression, not, OperatorEnum.OR);
  }

  default B orWhereNotIn(String column, QueryExpression expression) {
    return orWhereIn(column, expression, true);
  }

  default B exists(QueryExpression expression, OperatorEnum type) {
    return where(new ExistsPredicateExpression(expression, false, type));
  }

  default B exists(QueryExpression expression) {
    return exists(expression, OperatorEnum.AND);
  }

  default B notExists(QueryExpression expression, OperatorEnum type) {
    return where(new ExistsPredicateExpression(expression, true, type));
  }

  default B notExists(QueryExpression expression) {
    return notExists(expression, OperatorEnum.AND);
  }

  default B where(
      QueryExpression expression,
      PredicateOperatorEnum operator,
      List<Object> values,
      OperatorEnum type) {
    return where(expression, operator.getValue(), values, type);
  }

  default B where(
      QueryExpression expression, String operator, List<Object> values, OperatorEnum type) {
    return where(new CalculatedPredicateExpression(expression, operator, values, type));
  }

  default B where(QueryExpression expression, String operator, Object value, OperatorEnum type) {
    return where(expression, operator, List.of(value), type);
  }

  default B where(
      QueryExpression expression, PredicateOperatorEnum operator, Object value, OperatorEnum type) {
    return where(expression, operator.getValue(), value, type);
  }

  default B where(QueryExpression expression, String operator, Object value) {
    return where(expression, operator, value, OperatorEnum.AND);
  }

  default PredicateExpression getPredicateExpression(OperatorEnum type) {
    return COLLECTOR.collect(getPredicates(), type);
  }

  default PredicateExpression getPredicateExpression() {
    return getPredicateExpression(OperatorEnum.AND);
  }
}
