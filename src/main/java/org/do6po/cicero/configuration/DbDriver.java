package org.do6po.cicero.configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.do6po.cicero.exception.BaseException;
import org.do6po.cicero.exception.CiceroDbDriverException;
import org.do6po.cicero.expression.Expression;
import org.do6po.cicero.query.grammar.Grammar;

public interface DbDriver {
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
    try {
      PreparedStatement preparedStatement = connection.prepareStatement(sqlExpression);

      int i = 0;

      for (Object binding : bindings) {
        preparedStatement.setObject(++i, binding);
      }

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
              .formatted(
                  e.getMessage(),
                  e.getSQLState(),
                  sqlExpression,
                  bindings.stream().map(String::valueOf).collect(Collectors.joining(", ")));
      throw new BaseException(message, e);
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
}
