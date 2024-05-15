package org.do6po.cicero.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class Connector implements ConnectorInterface {
  public DbDriver connect(DbConfig config) {
    HikariConfig c = new HikariConfig();
    c.setJdbcUrl(config.getUrl());
    c.setUsername(config.getUsername());
    c.setPassword(config.getPassword());

    return new CiceroConnection(new HikariDataSource(c), GrammarFactory.get(config.getDriver()));
  }
}
