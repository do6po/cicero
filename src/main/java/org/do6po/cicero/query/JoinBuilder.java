package org.do6po.cicero.query;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.do6po.cicero.enums.PredicateOperatorEnum;
import org.do6po.cicero.expression.join.JoinExpression;
import org.do6po.cicero.expression.join.JoinExpression.JoinTypeEnum;
import org.do6po.cicero.expression.join.SimpleJoinExpression;
import org.do6po.cicero.expression.predicate.PredicateExpression;

@Getter
public class JoinBuilder implements CriteriaBuilder<JoinBuilder> {

  protected String table;
  protected String firstColumn;
  protected String operator = PredicateOperatorEnum.EQUALS.getValue();
  protected String secondColumn;
  protected JoinTypeEnum type = JoinTypeEnum.INNER_JOIN;

  protected List<PredicateExpression> predicates = new ArrayList<>();

  public static JoinBuilder join() {
    return new JoinBuilder();
  }

  public JoinExpression getJoinExpression() {
    return new SimpleJoinExpression(
        table, firstColumn, operator, secondColumn, type, getPredicateExpression());
  }

  public JoinBuilder table(String table) {
    this.table = table;

    return self();
  }

  public JoinBuilder on(String firstColumn, String operator, String secondColumn) {
    this.firstColumn = firstColumn;
    this.operator = operator;
    this.secondColumn = secondColumn;

    return self();
  }

  public JoinBuilder on(String firstColumn, String secondColumn) {
    return on(firstColumn, PredicateOperatorEnum.EQUALS.getValue(), secondColumn);
  }

  public JoinBuilder type(JoinTypeEnum type) {
    this.type = type;

    return self();
  }
}
