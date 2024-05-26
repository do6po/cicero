package org.do6po.cicero.configuration;

import java.util.HashMap;
import java.util.Map;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.do6po.cicero.exception.CiceroConnectionException;

@NoArgsConstructor
public class ConnectionResolver {

  private final Map<String, DbDriver> connections = new HashMap<>();

  public void put(String name, DbDriver connection) {
    if (connections.containsKey(name)) {
      throw new CiceroConnectionException(
          "Connection with name '%s' already exists!".formatted(name));
    }

    connections.put(name, connection);
  }

  public DbDriver get(@NonNull String name) {
    if (connections.containsKey(name)) {
      return connections.get(name);
    }

    throw new CiceroConnectionException("Connection '%s' not found!".formatted(name));
  }
}
