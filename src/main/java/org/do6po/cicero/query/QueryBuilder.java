package org.do6po.cicero.query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import org.do6po.cicero.utils.RowMapper;

public class QueryBuilder extends Builder<Map<String, Object>, QueryBuilder> {

  public static QueryBuilder query() {
    return new QueryBuilder();
  }

  public static QueryBuilder query(String table) {
    return query().from(table);
  }

  public static QueryBuilder query(String table, String as) {
    return query().from(table, as);
  }

  @Override
  public Map<String, Object> mapItem(ResultSet resultSet) throws SQLException {
    return RowMapper.rowToMap(resultSet);
  }
}
