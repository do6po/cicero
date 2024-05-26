package org.do6po.cicero.component;

import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.do6po.cicero.configuration.ConnectionResolver;
import org.do6po.cicero.configuration.DbDriver;
import org.do6po.cicero.exception.BaseException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConnectionResolverContainer {
  private static ConnectionResolver resolver;

  public static void put(ConnectionResolver resolver) {
    ConnectionResolverContainer.resolver = resolver;
  }

  public static ConnectionResolver get() {
    if (Objects.isNull(resolver)) {
      throw new BaseException("Resolver container is not set!");
    }

    return resolver;
  }

  public static boolean has() {
    return Objects.nonNull(resolver);
  }

  public static DbDriver get(String name) {
    return get().get(name);
  }
}
