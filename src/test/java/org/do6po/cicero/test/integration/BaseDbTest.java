package org.do6po.cicero.test.integration;

import static org.do6po.cicero.query.ModelQueryBuilder.query;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.darrmirr.dbchange.DbChangeExtension;
import com.github.darrmirr.dbchange.sql.executor.DefaultSqlExecutor;
import com.github.darrmirr.dbchange.sql.executor.SqlExecutor;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.Objects;
import javax.sql.DataSource;
import org.do6po.cicero.component.ConnectionResolverContainer;
import org.do6po.cicero.configuration.CiceroConnection;
import org.do6po.cicero.configuration.ConnectionResolver;
import org.do6po.cicero.interceptor.CiceroQueryCounter;
import org.do6po.cicero.interceptor.QueryCounter;
import org.do6po.cicero.test.integration.model.BrandM;
import org.do6po.cicero.test.integration.model.ProductM;
import org.do6po.cicero.test.integration.model.UserM;
import org.do6po.cicero.test.integration.model.builder.BrandQB;
import org.do6po.cicero.test.integration.model.builder.ProductQB;
import org.do6po.cicero.test.integration.model.builder.UserQB;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.postgresql.Driver;

@ExtendWith(DbChangeExtension.class)
public abstract class BaseDbTest {

  public static final String CONNECTION_NAME_DEFAULT = "default";

  public static final String DEFAULT_SQL_EXECUTOR = "defaultSqlExecutor";

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

  public static final String BRAND1_ID = "00c146fd-12f2-413b-b7b5-cbd5857586d6";
  public static final String BRAND2_ID = "55f88058-7c11-4b34-af7c-eee23ee97e22";
  public static final String BRAND3_ID = "add83dbb-ec7f-4853-b770-d59d394625ac";
  public static final String BRAND4_ID = "7d53d7e4-3abf-43bf-83d4-78c3f2f92bde";
  public static final String BRAND5_ID = "9b810916-b7c0-48b6-a9c9-e1905d82848f";

  public static final String PRODUCT1_ID = "116d9889-cc74-47da-b37b-3570ca4acb2b";
  public static final String PRODUCT2_ID = "a8eb7881-0ff8-4ce8-8565-7583d17081f9";
  public static final String PRODUCT3_ID = "43c92224-2a55-4ee8-bd83-31ac69c07c04";
  public static final String PRODUCT4_ID = "3e5a1062-9653-48ce-bac1-8fbfdad08175";
  public static final String PRODUCT5_ID = "744be470-0de0-484d-85e4-5939653c1561";
  public static final String PRODUCT6_ID = "556f75bc-c855-4339-ad75-5c9c8b3bd35b";
  public static final String PRODUCT7_ID = "34a6453a-df08-4f52-90ca-a3e3e7d38583";

  public static final String MEDIA1_ID = "d12bc519-e4bb-4f3f-95c4-a54f76a47dd6";
  public static final String MEDIA2_ID = "7991bcfa-0350-4ee2-b560-f23016baafd0";
  public static final String MEDIA3_ID = "37a830f1-b127-4ff9-b009-09d321b5e031";
  public static final String MEDIA4_ID = "9a223c97-6c4c-4cd5-a24e-c76461fa0970";
  public static final String MEDIA5_ID = "2c60f753-91a4-420d-83b3-f24b1b12ca2a";

  public static final String CATEGORY1_ID = "47d347e4-0c7b-47ce-975f-4f4f92b9ca0a";
  public static final String CATEGORY2_ID = "61252e97-80f4-4212-9ecb-76f3f7487718";
  public static final String CATEGORY3_ID = "4a52e36c-3d78-4c4b-a9e5-569251ea74ab";
  public static final String CATEGORY4_ID = "b1771a71-e956-4b13-a89b-eaa0be296866";

  protected static SqlExecutor sqlExecutor;
  protected static QueryCounter queryCounter;

  @BeforeAll
  static void setUpConnection() {
    if (ConnectionResolverContainer.has()) {
      return;
    }

    HikariConfig c = new HikariConfig();
    c.setJdbcUrl("jdbc:postgresql://localhost:25432/cicero");
    c.setUsername("root");
    c.setPassword("password");

    DataSource dataSource = new HikariDataSource(c);

    queryCounter = new CiceroQueryCounter();
    sqlExecutor = new DefaultSqlExecutor(dataSource);

    ConnectionResolver resolver = new ConnectionResolver();
    resolver.put(
        CONNECTION_NAME_DEFAULT, new CiceroConnection(dataSource, Driver.class, queryCounter));
    ConnectionResolverContainer.put(resolver);
  }

  protected static UserQB userQuery() {
    return query(UserM.class);
  }

  protected static ProductQB productQuery() {
    return query(ProductM.class);
  }

  protected static BrandQB brandQuery() {
    return query(BrandM.class);
  }

  @BeforeEach
  void setUp() {
    stopQueryCount();
  }

  protected void startQueryCount() {
    queryCounter.start();
  }

  protected Long getQueryCount() {
    return queryCounter.get();
  }

  protected void stopQueryCount() {
    queryCounter.stop();
  }

  protected void assertQueryCount(Integer count) {
    Long queryCount = getQueryCount();

    if (Objects.isNull(queryCount)) {
      throw new AssertionError("Query count is not started!");
    }

    assertEquals(Long.valueOf(count), queryCount);
  }

  public SqlExecutor defaultSqlExecutor() {
    return sqlExecutor;
  }
}
