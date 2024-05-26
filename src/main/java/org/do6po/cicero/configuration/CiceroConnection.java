package org.do6po.cicero.configuration;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Objects;
import javax.sql.DataSource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.do6po.cicero.interceptor.CiceroConnectionDelegate;
import org.do6po.cicero.interceptor.QueryCounter;
import org.do6po.cicero.query.grammar.Grammar;

@Slf4j
@Getter
@RequiredArgsConstructor
public class CiceroConnection implements DbDriver {
  private final DataSource dataSource;
  private final Grammar grammar;
  private final QueryCounter queryCounter;

  public CiceroConnection(DataSource dataSource, Class<? extends Driver> driverClass) {
    this(dataSource, driverClass, null);
  }

  public CiceroConnection(
      DataSource dataSource, Class<? extends Driver> driverClass, QueryCounter queryCounter) {
    this.dataSource = dataSource;
    this.grammar = GrammarFactory.get(driverClass);
    this.queryCounter = queryCounter;
  }

  @Override
  public Connection getConnection() throws SQLException {
    return Objects.isNull(queryCounter)
        ? dataSource.getConnection()
        : new CiceroConnectionDelegate(dataSource.getConnection(), queryCounter);
  }
}
