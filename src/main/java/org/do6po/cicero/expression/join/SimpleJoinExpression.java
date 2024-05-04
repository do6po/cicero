package org.do6po.cicero.expression.join;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import org.do6po.cicero.enums.PredicateOperatorEnum;
import org.do6po.cicero.expression.predicate.PredicateExpression;

public class SimpleJoinExpression implements JoinExpression {

  private final String table;
  private final String firstColumn;
  private final String operator;
  private final String secondColumn;
  @Getter private final JoinTypeEnum type;
  private final PredicateExpression predicateExpression;

  public SimpleJoinExpression(
      String table,
      String firstColumn,
      String operator,
      String secondColumn,
      JoinTypeEnum type,
      PredicateExpression predicateExpression) {
    this.table = table;
    this.firstColumn = firstColumn;
    this.operator = operator;
    this.secondColumn = secondColumn;
    this.type = type;
    this.predicateExpression = predicateExpression;
  }

  public SimpleJoinExpression(
      String table, String firstColumn, String operator, String secondColumn, JoinTypeEnum type) {
    this(table, firstColumn, operator, secondColumn, type, null);
  }

  public SimpleJoinExpression(
      String table, String firstColumn, String operator, String secondColumn) {
    this(table, firstColumn, operator, secondColumn, JoinTypeEnum.INNER_JOIN);
  }

  public SimpleJoinExpression(
      String table, String firstColumn, PredicateOperatorEnum operator, String secondColumn) {
    this(table, firstColumn, operator.name(), secondColumn, JoinTypeEnum.INNER_JOIN);
  }

  @Override
  public String getExpression() {
    String join =
        "%s %s ON %s %s %s".formatted(type.getValue(), table, firstColumn, operator, secondColumn);

    if (predicateExpression != null) {
      join +=
          " %s %s"
              .formatted(
                  predicateExpression.getType().getValue(), predicateExpression.getExpression());
    }

    return join;
  }

  @Override
  public Collection<Object> getBindings() {
    if (Objects.isNull(predicateExpression)) {
      return List.of();
    }

    return predicateExpression.getBindings();
  }
}
