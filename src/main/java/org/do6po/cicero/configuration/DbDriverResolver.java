package org.do6po.cicero.configuration;

import java.util.HashMap;
import java.util.Map;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.do6po.cicero.exception.CiceroDbDriverException;

@NoArgsConstructor
public class DbDriverResolver {

  private final Map<String, DbDriver> drivers = new HashMap<>();

  public DbDriverResolver put(String name, DbDriver connection) {
    if (drivers.containsKey(name)) {
      throw new CiceroDbDriverException(
          "Connection with name '%s' already exists!".formatted(name));
    }

    drivers.put(name, connection);

    return this;
  }

  public DbDriver get(@NonNull String name) {
    if (drivers.containsKey(name)) {
      return drivers.get(name);
    }

    throw new CiceroDbDriverException("Driver '%s' not found!".formatted(name));
  }
}
