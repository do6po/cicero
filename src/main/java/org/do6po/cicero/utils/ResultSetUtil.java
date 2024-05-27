package org.do6po.cicero.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.do6po.cicero.exception.FetchResultException;
import org.do6po.cicero.expression.SqlExpression;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResultSetUtil {

  public static ResultSet fetch(Connection connection, SqlExpression expression)
      throws SQLException {
    String sqlExpression = expression.getExpression();

    PreparedStatement preparedStatement = connection.prepareStatement(sqlExpression);

    int i = 1;

    for (Object binding : expression.getBindings()) {
      preparedStatement.setObject(i++, BindingNormalizeUtil.normalize(binding));
    }

    return preparedStatement.executeQuery();
  }

  public static <R> R fetchAndMap(
      Connection connection, SqlExpression expression, Function<ResultSet, R> mapper) {
    String sqlExpression = expression.getExpression();
    String bindingsAsString =
        expression.getBindings().stream().map(String::valueOf).collect(Collectors.joining(", "));

    try {
      ResultSet resultSet = fetch(connection, expression);

      log.debug(
          """
          Builder.fetchResultSet:
          Query: '%s'.
          Bindings: (%s)
          """
              .formatted(sqlExpression, bindingsAsString));

      return mapper.apply(resultSet);
    } catch (SQLException e) {
      String message =
          """
          Builder.fetchResultSet error:
          %s
          Sql state: '%s'.
          Query: '%s'.
          Bindings: (%s).
          """
              .formatted(e.getMessage(), e.getSQLState(), sqlExpression, bindingsAsString);

      log.error(message, e);

      throw new FetchResultException(message, e);
    }
  }

  @SneakyThrows(SQLException.class)
  public static @NonNull <T> List<T> mapList(
      ResultSet resultSet, Function<ResultSet, T> function, Integer chunk) {
    List<T> result = new ArrayList<>();
    while (resultSet.next()) {
      result.add(function.apply(resultSet));

      if (Objects.nonNull(chunk) && result.size() == chunk) {
        return result;
      }
    }

    return result;
  }
}
