package org.do6po.cicero.utils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RowMapper {

  public static Map<String, Object> rowToMap(ResultSet rs) {
    return rowToMap(rs, null);
  }

  @SneakyThrows
  public static Map<String, Object> rowToMap(ResultSet rs, String table) {
    Map<String, Object> result = new LinkedHashMap<>();

    ResultSetMetaData metaData = rs.getMetaData();
    for (int i = 1; i <= metaData.getColumnCount(); i++) {

      String tableName = metaData.getTableName(i);
      String tableColumn = metaData.getColumnName(i);

      if (tableName != null && !tableName.isEmpty() && !tableName.equals(table)) {
        tableColumn = tableName + "." + metaData.getColumnName(i);
      }

      result.put(tableColumn, rs.getObject(i));
    }

    return result;
  }
}
