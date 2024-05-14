package org.do6po.cicero.configuration;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.Getter;
import org.do6po.cicero.exception.CiceroConnectionException;
import org.do6po.cicero.interceptor.CiceroConnectionDelegate;
import org.do6po.cicero.interceptor.ConnectionInterceptor;
import org.do6po.cicero.query.grammar.Grammar;

@Getter
public class CiceroConnection implements DbDriver {
  private final DataSource dataSource;
  private final Connection connection;
  private final ConnectionInterceptor interceptor;
  private final Grammar grammar;

  public CiceroConnection(DataSource dataSource, Grammar grammar) {
    this.dataSource = dataSource;
    this.grammar = grammar;

    try {
      this.connection = dataSource.getConnection();
    } catch (SQLException e) {
      throw new CiceroConnectionException("Connection failed!", e);
    }

    this.interceptor = new CiceroConnectionDelegate(connection);
  }
}
