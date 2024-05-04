package org.do6po.cicero.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Connector {
  private final DbConfig config;

  public Connection getConnection() {
    HikariConfig c = new HikariConfig();
    c.setJdbcUrl(config.getUrl());
    c.setUsername(config.getUsername());
    c.setPassword(config.getPassword());

    HikariDataSource source = new HikariDataSource(c);

    try {
      return source.getConnection();
    } catch (SQLException e) {
      throw new RuntimeException("Connection failed!", e);
    }
  }
}
