package org.do6po.cicero.test.integration;

import static org.do6po.cicero.query.ModelQueryBuilder.query;

import com.github.darrmirr.dbchange.sql.executor.DefaultSqlExecutor;
import com.github.darrmirr.dbchange.sql.executor.SqlExecutor;
import com.zaxxer.hikari.HikariDataSource;
import java.util.Map;
import org.do6po.cicero.component.ConnectionResolverContainer;
import org.do6po.cicero.configuration.ConnectionResolver;
import org.do6po.cicero.configuration.DbConfig;
import org.do6po.cicero.configuration.SqlDriverEnum;
import org.do6po.cicero.test.integration.model.UserM;
import org.do6po.cicero.test.integration.model.builder.UserQB;
import org.junit.jupiter.api.BeforeAll;

public abstract class BaseDbTest {

  protected static SqlExecutor sqlExecutor;

  @BeforeAll
  static void configSqlExecutor() {
    HikariDataSource ds = new HikariDataSource();
    ds.setDriverClassName("org.postgresql.Driver");
    ds.setJdbcUrl("jdbc:postgresql://localhost:25432/cicero");
    ds.setUsername("root");
    ds.setPassword("password");

    sqlExecutor = new DefaultSqlExecutor(ds);
  }

  @BeforeAll
  static void configConnection() {
    DbConfig config = DbConfig.builder().driver(SqlDriverEnum.POSTGRESQL).database("cicero")
        .hostname("localhost").port(25432).username("root").password("password").build();

    ConnectionResolver resolver = new ConnectionResolver(Map.of("default", config));
    ConnectionResolverContainer.put(resolver);
  }

  protected static UserQB userQuery() {
    return query(UserM.class);
  }

  public SqlExecutor defaultSqlExecutor() {
    return sqlExecutor;
  }

}
