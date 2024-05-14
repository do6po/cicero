package org.do6po.cicero.component;

import java.util.Objects;
import org.do6po.cicero.configuration.ConnectionResolver;
import org.do6po.cicero.configuration.DbDriver;

public class ConnectionResolverContainer {
  private static ConnectionResolver resolver;

  public static void put(ConnectionResolver resolver) {
    ConnectionResolverContainer.resolver = resolver;
  }

  public static ConnectionResolver get() {
    if (Objects.isNull(resolver)) {
      throw new RuntimeException("Resolver container is not set!");
    }

    return resolver;
  }

  public static DbDriver getConnection(String name) {
    return get().getConnection(name);
  }
}
