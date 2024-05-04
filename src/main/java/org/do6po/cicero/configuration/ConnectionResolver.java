package org.do6po.cicero.configuration;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ConnectionResolver {

  // todo lazy connection

  private final Map<String, Connection> connections = new HashMap<>();

  public ConnectionResolver(Map<String, DbConfig> configs) {
    for (Entry<String, DbConfig> entry : configs.entrySet()) {
      putConnection(entry.getKey(), new Connector(entry.getValue()).getConnection());
    }
  }

  protected void putConnection(String name, Connection connection) {
    if (connections.containsKey(name)) {
      throw new RuntimeException("Connection with name '%s' already exists!".formatted(name));
    }

    connections.put(name, connection);
  }

  public Connection getConnection(String name) {
    if (connections.containsKey(name)) {
      return connections.get(name);
    }

    throw new RuntimeException("Connection %s not found!".formatted(name));
  }
}
