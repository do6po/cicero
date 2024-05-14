package org.do6po.cicero.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import org.do6po.cicero.exception.CiceroConnectionException;
import org.do6po.cicero.interceptor.CiceroConnectionDelegate;

@RequiredArgsConstructor
public class Connector {
  private final DbConfig config;

  public CiceroConnectionDelegate getConnection() {
    HikariConfig c = new HikariConfig();
    c.setJdbcUrl(config.getUrl());
    c.setUsername(config.getUsername());
    c.setPassword(config.getPassword());

    HikariDataSource source = new HikariDataSource(c);

    try {
      return new CiceroConnectionDelegate(source.getConnection());
    } catch (SQLException e) {
      throw new CiceroConnectionException("Connection failed!", e);
    }
  }
}
