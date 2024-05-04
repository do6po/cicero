package org.do6po.cicero.utils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RowMapper {

  public static Map<String, Object> rowToMap(ResultSet rs) throws SQLException {
    return rowToMap(rs, null);
  }

  public static Map<String, Object> rowToMap(ResultSet rs, String table) throws SQLException {
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
