package org.do6po.cicero.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.do6po.cicero.exception.CiceroConnectionException;
import org.do6po.cicero.interceptor.ConnectionInterceptor;

@NoArgsConstructor
public class ConnectionResolver {

  private final Map<String, DbDriver> connections = new HashMap<>();

  public ConnectionResolver(Map<String, DbConfig> configs) {
    for (Entry<String, DbConfig> entry : configs.entrySet()) {
      DbDriver connection = new Connector().connect(entry.getValue());

      putConnection(entry.getKey(), connection);
    }
  }

  protected void putConnection(String name, DbDriver connection) {
    if (connections.containsKey(name)) {
      throw new CiceroConnectionException(
          "Connection with name '%s' already exists!".formatted(name));
    }

    connections.put(name, connection);
  }

  public DbDriver getConnection(@NonNull String name) {
    if (connections.containsKey(name)) {
      return connections.get(name);
    }

    throw new CiceroConnectionException("Connection '%s' not found!".formatted(name));
  }

  public ConnectionInterceptor getInterceptor(@NonNull String name) {
    if (connections.containsKey(name)) {
      return connections.get(name).getInterceptor();
    }

    throw new CiceroConnectionException("Interceptor '%s' not found!".formatted(name));
  }
}
