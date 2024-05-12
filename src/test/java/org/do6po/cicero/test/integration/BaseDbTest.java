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

  public static final String USER1_ID = "4fbcfc50-7dda-4c1c-b358-30e70cb8b6d8";
  public static final String USER2_ID = "2249fa0d-766d-41e5-9b7a-2ca0961d1ab6";
  public static final String USER3_ID = "84409556-a609-4375-b5b6-44678009d193";
  public static final String USER4_ID = "2a03be2d-386d-45d6-bba2-8ce8afd81c5f";
  public static final String USER5_ID = "c0ec4c02-e22d-457a-982f-0f2b719d8142";
  public static final String USER6_ID = "6e8393d1-1f0d-4d11-9708-2ef3ad1ae060";

  public static final String USER1_ORDER1_ID = "028cd14d-a85f-40d8-b406-2887586db371";
  public static final String USER1_ORDER2_ID = "558bcdac-fbfd-4819-ad0e-f55310ea9e31";
  public static final String USER2_ORDER1_ID = "01d45da1-4b83-43b3-a40e-94a383fb9892";
  public static final String USER2_ORDER2_ID = "532cd78f-3289-4f64-8307-5664a2645e07";
  public static final String USER2_ORDER3_ID = "a0e21a4a-5d2b-49dd-a4c0-c6b3e56d047b";
  public static final String USER6_ORDER1_ID = "44cf0f62-b76f-4372-83f7-99ce1b5b96f9";

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
    DbConfig config =
        DbConfig.builder()
            .driver(SqlDriverEnum.POSTGRESQL)
            .database("cicero")
            .hostname("localhost")
            .port(25432)
            .username("root")
            .password("password")
            .build();

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
