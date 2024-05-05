package org.do6po.cicero.collector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import org.do6po.cicero.expression.Expression;
import org.do6po.cicero.expression.SimpleSqlExpression;
import org.do6po.cicero.expression.SqlExpression;
import org.do6po.cicero.expression.from.FromExpression;
import org.do6po.cicero.expression.groupby.GroupByExpression;
import org.do6po.cicero.expression.having.HavingExpression;
import org.do6po.cicero.expression.join.JoinExpression;
import org.do6po.cicero.expression.order.OrderExpression;
import org.do6po.cicero.expression.select.SelectExpression;
import org.do6po.cicero.expression.union.UnionExpression;
import org.do6po.cicero.query.Builder;

public class PgsqlQueryCollector implements QueryCollector {

  @Override
  public SqlExpression collectExpression(Builder<?, ?> builder) {
    StringBuilder sqlStringBuilder = new StringBuilder();
    ArrayList<Object> bindingResult = new ArrayList<>();

    fillForSelect(builder, sqlStringBuilder, bindingResult);
    addSpace(sqlStringBuilder);
    fillForFrom(builder, sqlStringBuilder, bindingResult);

    if (!builder.getJoins().isEmpty()) {
      addSpace(sqlStringBuilder);
      fillJoin(builder, sqlStringBuilder, bindingResult);
    }

    if (!builder.getPredicates().isEmpty()) {
      addSpace(sqlStringBuilder);
      fillPredicates(builder, sqlStringBuilder, bindingResult);
    }

    if (!builder.getGroups().isEmpty()) {
      addSpace(sqlStringBuilder);
      fillGroupBy(builder, sqlStringBuilder, bindingResult);
    }

    if (!builder.getHaving().isEmpty()) {
      addSpace(sqlStringBuilder);
      fillHaving(builder, sqlStringBuilder, bindingResult);
    }

    if (!builder.getUnions().isEmpty()) {
      addSpace(sqlStringBuilder);
      fillForUnion(builder, sqlStringBuilder, bindingResult);
    }

    if (!builder.getOrders().isEmpty()) {
      addSpace(sqlStringBuilder);
      fillForOrders(builder, sqlStringBuilder, bindingResult);
    }

    if (builder.getLimit() != null) {
      addSpace(sqlStringBuilder);
      fillForLimit(builder, sqlStringBuilder);
    }

    if (builder.getOffset() > 0) {
      addSpace(sqlStringBuilder);
      fillForOffset(builder, sqlStringBuilder);
    }

    String sqlRaw = sqlStringBuilder.toString();
    sqlRaw = sqlRaw.replaceAll("\\s+", " ");

    return new SimpleSqlExpression(sqlRaw, bindingResult);
  }

  private void fillJoin(
      Builder<?, ?> builder, StringBuilder sqlStringBuilder, ArrayList<Object> bindingResult) {
    List<JoinExpression> joins = builder.getJoins();
    if (joins.isEmpty()) {
      return;
    }

    Iterator<JoinExpression> iterator = joins.iterator();
    while (iterator.hasNext()) {
      JoinExpression join = iterator.next();
      sqlStringBuilder.append(join.getExpression());

      if (iterator.hasNext()) {
        sqlStringBuilder.append(" ");
      }

      bindingResult.addAll(join.getBindings());
    }
  }

  private void fillPredicates(
      Builder<?, ?> builder, StringBuilder sqlStringBuilder, ArrayList<Object> bindingResult) {
    sqlStringBuilder.append("WHERE ");

    Expression predicateExpression = builder.getPredicateExpression();

    sqlStringBuilder.append(predicateExpression.getExpression());
    bindingResult.addAll(predicateExpression.getBindings());
  }

  protected void fillGroupBy(
      Builder<?, ?> builder, StringBuilder sqlStringBuilder, ArrayList<Object> bindingResult) {
    List<GroupByExpression> groups = builder.getGroups();
    if (groups.isEmpty()) {
      return;
    }

    sqlStringBuilder.append("GROUP BY ");

    Iterator<GroupByExpression> iterator = groups.iterator();
    while (iterator.hasNext()) {
      GroupByExpression group = iterator.next();
      sqlStringBuilder.append(group.getExpression());

      if (iterator.hasNext()) {
        sqlStringBuilder.append(", ");
      }

      bindingResult.addAll(group.getBindings());
    }
  }

  protected void fillHaving(
      Builder<?, ?> builder, StringBuilder sqlStringBuilder, ArrayList<Object> bindingResult) {
    List<HavingExpression> having = builder.getHaving();
    if (having.isEmpty()) {
      return;
    }

    sqlStringBuilder.append("HAVING ");

    ListIterator<HavingExpression> iterator = having.listIterator();
    while (iterator.hasNext()) {
      boolean hasPrevious = iterator.hasPrevious();

      HavingExpression item = iterator.next();

      if (hasPrevious) {
        sqlStringBuilder.append(" %s ".formatted(item.getType().getValue()));
      }

      sqlStringBuilder.append(item.getExpression());

      bindingResult.addAll(item.getBindings());
    }
  }

  protected void fillForOffset(Builder<?, ?> builder, StringBuilder sqlStringBuilder) {
    Integer offset = builder.getOffset();
    if (offset == 0) {
      return;
    }

    sqlStringBuilder.append("OFFSET %s".formatted(offset));
  }

  protected static StringBuilder addSpace(StringBuilder sqlStringBuilder) {
    return sqlStringBuilder.append(" ");
  }

  protected void fillForSelect(
      Builder<?, ?> builder, StringBuilder sqlStringBuilder, ArrayList<Object> bindingResult) {
    sqlStringBuilder.append("SELECT ");

    if (builder.isDistinct()) {
      sqlStringBuilder.append("DISTINCT ");
    }

    List<SelectExpression> columns = builder.getColumns();
    if (columns.isEmpty()) {
      sqlStringBuilder.append("*");
      return;
    }

    Iterator<SelectExpression> iterator = columns.iterator();
    while (iterator.hasNext()) {
      SelectExpression selection = iterator.next();
      sqlStringBuilder.append(selection.getExpression());

      if (iterator.hasNext()) {
        sqlStringBuilder.append(", ");
      }

      bindingResult.addAll(selection.getBindings());
    }
  }

  protected void fillForFrom(
      Builder<?, ?> builder, StringBuilder sqlStringBuilder, ArrayList<Object> bindingResult) {
    List<FromExpression> from = builder.getFrom();
    if (from.isEmpty()) {
      throw new RuntimeException("Table is not specified!");
    }

    sqlStringBuilder.append("FROM ");

    Iterator<FromExpression> fromIterator = from.iterator();

    while (fromIterator.hasNext()) {
      FromExpression fromExpression = fromIterator.next();
      sqlStringBuilder.append(fromExpression.getExpression());

      if (fromIterator.hasNext()) {
        sqlStringBuilder.append(", ");
      }

      bindingResult.addAll(fromExpression.getBindings());
    }
  }

  private void fillForOrders(
      Builder<?, ?> builder, StringBuilder sqlStringBuilder, ArrayList<Object> bindingResult) {
    List<OrderExpression> orders = builder.getOrders();
    if (orders.isEmpty()) {
      return;
    }

    sqlStringBuilder.append("ORDER BY ");

    Iterator<OrderExpression> orderIterator = orders.iterator();

    while (orderIterator.hasNext()) {
      OrderExpression order = orderIterator.next();
      sqlStringBuilder.append(order.getExpression());

      if (orderIterator.hasNext()) {
        sqlStringBuilder.append(", ");
      }

      bindingResult.addAll(order.getBindings());
    }
  }

  protected void fillForUnion(
      Builder<?, ?> builder, StringBuilder sqlStringBuilder, ArrayList<Object> bindingResult) {
    List<UnionExpression> unions = builder.getUnions();
    if (unions.isEmpty()) {
      return;
    }

    Iterator<UnionExpression> unionIterator = unions.iterator();

    while (unionIterator.hasNext()) {
      UnionExpression item = unionIterator.next();
      sqlStringBuilder.append("%s ".formatted(item.getType().getValue()));
      sqlStringBuilder.append(item.getExpression());

      bindingResult.addAll(item.getBindings());
    }
  }

  protected void fillForLimit(Builder<?, ?> builder, StringBuilder sqlStringBuilder) {
    Integer limit = builder.getLimit();
    if (Objects.isNull(limit)) {
      return;
    }

    sqlStringBuilder.append("LIMIT %d".formatted(limit));
  }
}
