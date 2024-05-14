package org.do6po.cicero.configuration;

public interface ConnectorInterface {
  DbDriver connect(DbConfig config);
}
