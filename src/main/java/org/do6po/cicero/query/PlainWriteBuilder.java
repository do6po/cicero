package org.do6po.cicero.query;

import java.sql.ResultSet;
import java.util.Map;
import org.do6po.cicero.utils.RowMapper;

public class PlainWriteBuilder extends WriteBuilder<Map<String, Object>, PlainWriteBuilder> {

  public static PlainWriteBuilder query() {
    return new PlainWriteBuilder();
  }

  public static PlainWriteBuilder query(String table) {
    return query().from(table);
  }

  @Override
  public Map<String, Object> mapItem(ResultSet resultSet) {
    return RowMapper.rowToMap(resultSet);
  }
}
