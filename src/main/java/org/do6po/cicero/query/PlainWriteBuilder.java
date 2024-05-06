package org.do6po.cicero.query;

import org.do6po.cicero.query.grammar.Grammar;
import org.do6po.cicero.query.grammar.PostgresGrammar;
import org.do6po.cicero.utils.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class PlainWriteBuilder extends WriteBuilder<Map<String, Object>, PlainWriteBuilder> {
  public static PlainWriteBuilder query(String table) {
    return query(table, new PostgresGrammar());
  }

  public static PlainWriteBuilder query(String table, Grammar grammar) {
    PlainWriteBuilder query = new PlainWriteBuilder();
    query.from(table);
    query.setGrammar(grammar);

    return query;
  }

  @Override
  public Map<String, Object> mapItem(ResultSet resultSet) throws SQLException {
    return RowMapper.rowToMap(resultSet);
  }
}
