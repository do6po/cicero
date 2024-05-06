package org.do6po.cicero.query;

import lombok.Setter;
import org.do6po.cicero.exception.BaseException;
import org.do6po.cicero.expression.Expression;
import org.do6po.cicero.expression.write.WriteExpression;
import org.do6po.cicero.query.grammar.Grammar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class WriteBuilder<T, B extends WriteBuilder<T, B>> extends Builder<T, B> {
  protected String table;

  @Setter protected Grammar grammar;

  protected void init() {}

  public int insert(Map<String, Object> value) {
    WriteExpression sqlExpresion = grammar.compileInsert(this, value);

    return executeQuery(sqlExpresion);
  }

  protected int executeQuery(Expression expression) {
    String sqlExpression = expression.getExpression();
    Collection<Object> bindings = expression.getBindings();
    String bindingAsString =
        bindings.stream().map(String::valueOf).collect(Collectors.joining(", "));

    try {
      Connection DBConnection = getConnection();
      PreparedStatement preparedStatement = DBConnection.prepareStatement(sqlExpression);

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
              .formatted(e.getMessage(), e.getSQLState(), sqlExpression, bindingAsString);
      throw new BaseException(message, e);
    }
  }
}
