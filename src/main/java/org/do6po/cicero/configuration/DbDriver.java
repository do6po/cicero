package org.do6po.cicero.configuration;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.do6po.cicero.exception.CiceroDbDriverException;
import org.do6po.cicero.expression.Expression;
import org.do6po.cicero.expression.SqlExpression;
import org.do6po.cicero.query.grammar.Grammar;
import org.do6po.cicero.utils.BindingNormalizeUtil;
import org.slf4j.Logger;

public interface DbDriver {
  Logger logger();

  Connection getConnection() throws SQLException;

  Grammar getGrammar();

  default int executeWriteQuery(Function<DbDriver, Expression> function) {
    return executeWriteQuery(function.apply(this));
  }

  default int executeWriteQuery(Expression expression) {
    return execute(conn -> executeWriteQuery(conn, expression));
  }

  default int executeWriteQuery(Connection connection, Expression expression) {
    String sqlExpression = expression.getExpression();
    Collection<Object> bindings = expression.getBindings();
    String bindingsAsString =
        bindings.stream().map(String::valueOf).collect(Collectors.joining(", "));

    try {
      PreparedStatement preparedStatement = connection.prepareStatement(sqlExpression);

      int i = 0;

      for (Object binding : bindings) {
        preparedStatement.setObject(++i, binding);
      }

      logger()
          .debug(
              """
              Builder.executeWriteQuery:
              Query: '{}'.
              Bindings: ({})
              """,
              sqlExpression,
              bindingsAsString);

      return preparedStatement.executeUpdate();
    } catch (SQLException e) {
      String message =
          """
          Builder.executeQuery error:
          %s
          Sql state: '%s'.
          Query: '%s'.
          Bindings: (%s).
          """
              .formatted(e.getMessage(), e.getSQLState(), sqlExpression, bindingsAsString);

      logger().error(message, e);

      throw new CiceroDbDriverException(message, e);
    }
  }

  default <T> T execute(Function<Connection, T> function) {
    try (Connection connection = getConnection()) {
      return function.apply(connection);
    } catch (SQLException e) {
      String message = "Connection failed!";
      throw new CiceroDbDriverException(message, e);
    }
  }

  default ResultSet createCursor(
      Connection connection, SqlExpression expression, StatementConsumer hook) {
    String sqlExpression = expression.getExpression();
    Collection<Object> bindings = expression.getBindings();
    String bindingsAsString =
        bindings.stream().map(String::valueOf).collect(Collectors.joining(", "));

    try {
      connection.setAutoCommit(false);

      CallableStatement callableStatement = connection.prepareCall(sqlExpression);

      int i = 1;

      for (Object binding : bindings) {
        callableStatement.setObject(i++, BindingNormalizeUtil.normalize(binding));
      }

      hook.accept(callableStatement);

      ResultSet resultSet = callableStatement.executeQuery();

      logger()
          .debug(
              """
              Builder.createCursor:
              Query: '{}'.
              Bindings: ({})
              """,
              sqlExpression,
              bindingsAsString);

      return resultSet;
    } catch (SQLException e) {
      String message =
          """
          Builder.createCursor error:
          %s
          Sql state: '%s'.
          Query: '%s'.
          Bindings: (%s).
          """
              .formatted(e.getMessage(), e.getSQLState(), sqlExpression, bindingsAsString);

      logger().error(message, e);

      throw new CiceroDbDriverException(message, e);
    }
  }

  default <R> R executeFetch(SqlExpression expression, Function<ResultSet, R> mapper) {
    return execute(c -> executeFetch(c, expression, mapper));
  }

  default <R> R executeFetch(Connection connection, SqlExpression expression, Function<ResultSet, R> mapper) {
    String sqlExpression = expression.getExpression();
    String bindingsAsString =
        expression.getBindings().stream().map(String::valueOf).collect(Collectors.joining(", "));

    try {
      PreparedStatement preparedStatement = connection.prepareStatement(sqlExpression);

      int i = 1;

      for (Object binding : expression.getBindings()) {
        preparedStatement.setObject(i++, BindingNormalizeUtil.normalize(binding));
      }

      ResultSet resultSet = preparedStatement.executeQuery();

      logger()
          .debug(
              """
              Builder.executeFetch:
              Query: '{}'.
              Bindings: ({})
              """,
              sqlExpression,
              bindingsAsString);

      return mapper.apply(resultSet);
    } catch (SQLException e) {
      String message =
          """
          Builder.executeFetch error:
          %s
          Sql state: '%s'.
          Query: '%s'.
          Bindings: (%s).
          """
              .formatted(e.getMessage(), e.getSQLState(), sqlExpression, bindingsAsString);

      logger().error(message, e);

      throw new CiceroDbDriverException(message, e);
    }
  }

  interface StatementConsumer {
    void accept(CallableStatement cs) throws SQLException;
  }
}
