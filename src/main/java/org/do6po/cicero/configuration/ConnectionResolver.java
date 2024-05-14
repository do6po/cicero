package org.do6po.cicero.configuration;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.do6po.cicero.exception.CiceroConnectionException;
import org.do6po.cicero.interceptor.CiceroConnectionDelegate;
import org.do6po.cicero.interceptor.CiceroConnectionInterceptor;

@NoArgsConstructor
public class ConnectionResolver {

  private final Map<String, CiceroConnectionDelegate> connections = new HashMap<>();

  public ConnectionResolver(Map<String, DbConfig> configs) {
    for (Entry<String, DbConfig> entry : configs.entrySet()) {
      CiceroConnectionDelegate connection = new Connector(entry.getValue()).getConnection();

      putConnection(entry.getKey(), connection);
    }
  }

  protected void putConnection(String name, CiceroConnectionDelegate connection) {
    if (connections.containsKey(name)) {
      throw new CiceroConnectionException(
          "Connection with name '%s' already exists!".formatted(name));
    }

    connections.put(name, connection);
  }

  public Connection getConnection(@NonNull String name) {
    if (connections.containsKey(name)) {
      return connections.get(name);
    }

    throw new CiceroConnectionException("Connection '%s' not found!".formatted(name));
  }

  public CiceroConnectionInterceptor getInterceptor(@NonNull String name) {
    if (connections.containsKey(name)) {
      return connections.get(name);
    }

    throw new CiceroConnectionException("Interceptor '%s' not found!".formatted(name));
  }
}
