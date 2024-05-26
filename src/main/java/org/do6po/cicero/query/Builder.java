package org.do6po.cicero.query;

import static org.do6po.cicero.utils.ClassUtil.getInstance;
import static org.do6po.cicero.utils.ClassUtil.guessType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.do6po.cicero.component.ConnectionResolverContainer;
import org.do6po.cicero.configuration.DbDriver;
import org.do6po.cicero.enums.DirectionEnum;
import org.do6po.cicero.enums.OperatorEnum;
import org.do6po.cicero.enums.PredicateOperatorEnum;
import org.do6po.cicero.enums.UnionTypeEnum;
import org.do6po.cicero.exception.FetchResultException;
import org.do6po.cicero.exception.PaginateException;
import org.do6po.cicero.expression.SqlExpression;
import org.do6po.cicero.expression.from.FromExpression;
import org.do6po.cicero.expression.from.QueryFromExpression;
import org.do6po.cicero.expression.from.SimpleFromExpression;
import org.do6po.cicero.expression.groupby.GroupByExpression;
import org.do6po.cicero.expression.groupby.SimpleGroupByExpression;
import org.do6po.cicero.expression.having.HavingExpression;
import org.do6po.cicero.expression.having.SimpleHavingExpression;
import org.do6po.cicero.expression.having.StringHavingExpression;
import org.do6po.cicero.expression.join.JoinExpression;
import org.do6po.cicero.expression.join.JoinExpression.JoinTypeEnum;
import org.do6po.cicero.expression.join.SimpleJoinExpression;
import org.do6po.cicero.expression.order.OrderExpression;
import org.do6po.cicero.expression.order.SimpleOrderExpression;
import org.do6po.cicero.expression.predicate.PredicateExpression;
import org.do6po.cicero.expression.select.RawSelectExpression;
import org.do6po.cicero.expression.select.SelectExpression;
import org.do6po.cicero.expression.select.SimpleSelectExpression;
import org.do6po.cicero.expression.select.SubSelectExpression;
import org.do6po.cicero.expression.subquery.QueryExpression;
import org.do6po.cicero.expression.union.QueryUnionExpression;
import org.do6po.cicero.expression.union.UnionExpression;
import org.do6po.cicero.iterator.ResultSetChunkIterator;
import org.do6po.cicero.pagination.Paginator;
import org.do6po.cicero.pagination.SimplePaginator;
import org.do6po.cicero.utils.BindingNormalizeUtil;

@Slf4j
public abstract class Builder<T, B extends Builder<T, B>>
    implements CriteriaBuilder<B>, QueryExpression {

  public static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

  public static int DEFAULT_CHUNK = 100;
  public static String COUNT_ALIAS = "count";

  protected String connection = "default";

  @Getter protected boolean parallel = true;
  @Getter protected List<SelectExpression> columns = new ArrayList<>();
  @Getter protected boolean distinct = false;
  @Getter protected List<FromExpression> from = new ArrayList<>();
  @Getter protected List<JoinExpression> joins = new ArrayList<>();
  @Getter protected List<PredicateExpression> predicates = new ArrayList<>();
  @Getter protected List<GroupByExpression> groups = new ArrayList<>();
  @Getter protected List<HavingExpression> having = new ArrayList<>();
  @Getter protected List<OrderExpression> orders = new ArrayList<>();
  @Getter protected List<UnionExpression> unions = new ArrayList<>();
  protected List<String> aggregate = new ArrayList<>();
  protected List<Object> groupLimit;
  protected int unionLimit;
  protected int unionOffset;
  protected List<Object> unionOrders;
  @Getter protected Integer limit;
  @Getter protected Integer offset = 0;

  public B distinct(boolean value) {
    distinct = value;

    return self();
  }

  protected B getBuilderInstance() {
    return getInstance(guessType(getClass(), 1));
  }

  public B parallel() {
    return parallel(true);
  }

  public B cuncurrent() {
    return parallel(false);
  }

  public B parallel(boolean parallel) {
    this.parallel = parallel;

    return self();
  }

  public B connection(String value) {
    connection = value;

    return self();
  }

  public B select(String... columns) {
    return select(new SimpleSelectExpression(Arrays.asList(columns)));
  }

  public B select(SelectExpression expression) {
    columns = new ArrayList<>();
    columns.add(expression);

    return self();
  }

  public B selectRaw(String raw, Collection<Object> bindings) {
    return select(new RawSelectExpression(raw, bindings));
  }

  public B addSelectRaw(String raw, Collection<Object> bindings) {
    return addSelect(new RawSelectExpression(raw, bindings));
  }

  public B addSelectRaw(String raw) {
    return addSelect(new RawSelectExpression(raw, List.of()));
  }

  public B addSelect(String... columns) {
    return addSelect(new SimpleSelectExpression(Arrays.asList(columns)));
  }

  public B addSelect(SelectExpression... expression) {
    columns.addAll(Arrays.asList(expression));

    return self();
  }

  public B addSelect(QueryExpression sqlExpression, String as) {
    columns.add(new SubSelectExpression(sqlExpression, as));

    return self();
  }

  public B from(String table, String as) {
    return from(new SimpleFromExpression(table, as));
  }

  public B from(String table) {
    return from(table, null);
  }

  public B from(FromExpression... expression) {
    return from(Arrays.asList(expression));
  }

  public B from(List<FromExpression> expressions) {
    from = new ArrayList<>(expressions);

    return self();
  }

  public B from(QueryExpression expression, String as) {
    return from(new QueryFromExpression(expression, as));
  }

  public B addFrom(FromExpression expression) {
    from.add(expression);

    return self();
  }

  public B addFrom(String table, String as) {
    return addFrom(new SimpleFromExpression(table, as));
  }

  public B addFrom(String table) {
    return addFrom(new SimpleFromExpression(table, null));
  }

  public B addFrom(QueryExpression expression, String as) {
    return addFrom(new QueryFromExpression(expression, as));
  }

  public B join(JoinExpression expression) {
    joins.add(expression);

    return self();
  }

  public B join(Consumer<JoinBuilder> function) {
    JoinBuilder join = new JoinBuilder();
    function.accept(join);

    return join(join.getJoinExpression());
  }

  public B join(String table, String first, String operator, String second, JoinTypeEnum joinType) {
    return join(new SimpleJoinExpression(table, first, operator, second, joinType));
  }

  public B innerJoin(String table, String first, String operator, String second) {
    return join(table, first, operator, second, JoinTypeEnum.INNER_JOIN);
  }

  public B innerJoin(String table, String first, String second) {
    return join(
        table, first, PredicateOperatorEnum.EQUALS.getValue(), second, JoinTypeEnum.INNER_JOIN);
  }

  public B leftJoin(String table, String first, String operator, String second) {
    return join(table, first, operator, second, JoinTypeEnum.LEFT_OUTER);
  }

  public B leftJoin(String table, String first, String second) {
    return join(
        table, first, PredicateOperatorEnum.EQUALS.getValue(), second, JoinTypeEnum.LEFT_OUTER);
  }

  public B rightJoin(String table, String first, String operator, String second) {
    return join(table, first, operator, second, JoinTypeEnum.RIGHT_OUTER);
  }

  public B exists(Consumer<B> consumer) {
    B builder = getBuilderInstance();
    consumer.accept(builder);
    return exists(builder);
  }

  public B having(HavingExpression expression) {
    having.add(expression);

    return self();
  }

  public B having(String column, String operator, List<Object> bindings, OperatorEnum type) {
    return having(new SimpleHavingExpression(column, operator, bindings, type));
  }

  public B having(String column, String operator, Object binding) {
    return having(new SimpleHavingExpression(column, operator, List.of(binding), OperatorEnum.AND));
  }

  public B havingRaw(String raw, List<Object> bindings, OperatorEnum type) {
    return having(new StringHavingExpression(raw, bindings, type));
  }

  public B havingRaw(String raw, List<Object> bindings) {
    return havingRaw(raw, bindings, OperatorEnum.AND);
  }

  public List<T> get() {
    return fetchResultSet(this::mapList);
  }

  public <R> R fetchResultSet(Function<ResultSet, R> function) {
    SqlExpression expression = getSqlExpression();
    String sqlExpression = expression.getExpression();
    Collection<Object> bindings = expression.getBindings();
    String bindingAsString =
        bindings.stream().map(String::valueOf).collect(Collectors.joining(", "));

    return getDbDriver()
        .execute(
            conn -> {
              try {

                PreparedStatement preparedStatement = conn.prepareStatement(sqlExpression);

                int i = 1;

                for (Object binding : bindings) {
                  preparedStatement.setObject(i++, BindingNormalizeUtil.normalize(binding));
                }

                ResultSet resultSet = preparedStatement.executeQuery();

                log.debug(
                    """
                    Builder.fetchResultSet:
                    Query: '%s'.
                    Bindings: '%s'
                    """
                        .formatted(sqlExpression, bindings));

                return function.apply(resultSet);

              } catch (SQLException e) {
                String message =
                    """
                    Builder.fetchResultSet error:
                    %s
                    Sql state: '%s'.
                    Query: '%s'.
                    Bindings: (%s).
                    """
                        .formatted(e.getMessage(), e.getSQLState(), sqlExpression, bindingAsString);

                log.error(message, e);

                throw new FetchResultException(message, e);
              }
            });
  }

  protected List<T> mapList(ResultSet resultSet) {
    return mapList(resultSet, null);
  }

  @SneakyThrows(SQLException.class)
  protected @NonNull List<T> mapList(ResultSet resultSet, Integer chunk) {
    List<T> result = new ArrayList<>();
    while (resultSet.next()) {
      result.add(mapItem(resultSet));

      if (Objects.nonNull(chunk) && result.size() == chunk) {
        return result;
      }
    }

    return result;
  }

  abstract T mapItem(ResultSet resultSet) throws SQLException;

  public Paginator<T> paginate(int page, int perPage) {
    try {
      Callable<List<T>> getItems = () -> perPage(perPage).offset((page - 1) * perPage).get();
      Callable<Long> getTotal = this::getCountForPagination;

      List<T> items;
      Long total;

      if (isParallel()) {
        Future<List<T>> futureItems = EXECUTOR.submit(getItems);
        Future<Long> futureTotal = EXECUTOR.submit(getTotal);

        items = futureItems.get();
        total = futureTotal.get();
      } else {
        items = getItems.call();
        total = getTotal.call();
      }

      return new SimplePaginator<>(page, perPage, total, items);
    } catch (Exception e) {
      String message = "Builder.paginate error!";
      log.error(message, e);
      throw new PaginateException(message, e);
    }
  }

  public Paginator<T> paginate() {
    return paginate(limit, offset);
  }

  public Long getCountForPagination() {
    B builder =
        getBuilderInstance()
            .from(from)
            // join
            .select("count(*) as %s".formatted(COUNT_ALIAS));

    builder.distinct = distinct;
    builder.predicates = new ArrayList<>(predicates);
    return builder.one().fetchResultSet(this::extractCountFromResultSet);
  }

  @SneakyThrows(SQLException.class)
  protected Long extractCountFromResultSet(ResultSet resultSet) {
    resultSet.next();
    return resultSet.getLong(COUNT_ALIAS);
  }

  public Optional<T> first() {
    return one().get().stream().findFirst();
  }

  public T firstOrThrow(Supplier<? extends RuntimeException> supplier) {
    return first().orElseThrow(supplier);
  }

  public T firstOrThrow() {
    return firstOrThrow(() -> new NoSuchElementException("No value present"));
  }

  public Iterator<List<T>> chunk() {
    return chunk(DEFAULT_CHUNK);
  }

  public Iterator<List<T>> chunk(Integer chunk) {
    return fetchResultSet(
        resultSet -> new ResultSetChunkIterator<>(() -> mapList(resultSet, chunk)));
  }

  public SqlExpression getSqlExpression() {
    return getDbDriver().getGrammar().getQueryCollector().collectExpression(self());
  }

  public DbDriver getDbDriver() {
    return ConnectionResolverContainer.get(connection);
  }

  public B limit(Integer i) {
    limit = i;

    return self();
  }

  public B unlimit() {
    return limit(null);
  }

  public B one() {
    return limit(1);
  }

  public B perPage(int i) {
    return limit(i);
  }

  public B offset(int i) {
    offset = i;

    return self();
  }

  public B orderBy(String column) {
    return orderBy(column, DirectionEnum.ASC);
  }

  public B orderBy(String column, DirectionEnum direction) {
    return orderBy(new SimpleOrderExpression(column, direction));
  }

  public B orderBy(OrderExpression order) {
    orders.add(order);

    return self();
  }

  public B groupBy(GroupByExpression expression) {
    groups.add(expression);

    return self();
  }

  public B groupBy(String... columns) {
    return groupBy(new SimpleGroupByExpression(Arrays.asList(columns)));
  }

  public B union(UnionExpression expression) {
    unions.add(expression);

    return self();
  }

  public B union(QueryExpression expression) {
    return union(new QueryUnionExpression(expression, UnionTypeEnum.UNION));
  }

  public B unionAll(QueryExpression expression) {
    return union(new QueryUnionExpression(expression, UnionTypeEnum.UNION_ALL));
  }

  public boolean exists() {
    return select("'true'").one().first().isPresent();
  }

  public boolean doesNotExists() {
    return !exists();
  }

  public interface ResultSetExecutor<V, R> {
    R execute(V value) throws SQLException;
  }
}
