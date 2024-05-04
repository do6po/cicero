package org.do6po.cicero.configuration;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DbConfig {
  @Builder.Default private SqlDriverEnum driver = SqlDriverEnum.POSTGRESQL;
  @Builder.Default private String hostname = "localhost";
  private String database;
  @Builder.Default private Integer port = 5432;
  @Builder.Default private boolean ssl = true;
  private String username;
  private String password;

  public String getUrl() {
    return "jdbc:%s://%s:%s/%s".formatted(driver.getType(), hostname, port, database);
  }
}
