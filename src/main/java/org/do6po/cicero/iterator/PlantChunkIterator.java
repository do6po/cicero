package org.do6po.cicero.iterator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import lombok.Getter;
import lombok.SneakyThrows;
import org.do6po.cicero.configuration.DbDriver;
import org.do6po.cicero.query.Builder;
import org.do6po.cicero.utils.ResultSetUtil;

public class PlantChunkIterator<T, B extends Builder<T, B>> implements ChunkIterator<T, B> {

  @Getter protected final B builder;
  protected final int size;
  protected final Connection connection;
  protected final ResultSet resultSet;

  public PlantChunkIterator(B builder, int size) throws SQLException {
    this.builder = builder;
    this.size = size;

    DbDriver dbDriver = builder.getDbDriver();
    this.connection = dbDriver.getConnection();
    this.resultSet =
        dbDriver.createCursor(connection, builder.getSqlExpression(), cs -> cs.setFetchSize(size));
  }

  @Override
  @SneakyThrows
  public boolean hasNext() {
    return !resultSet.isAfterLast();
  }

  @Override
  public List<T> next() {
    return ResultSetUtil.mapList(resultSet, builder::mapItem, size);
  }

  @Override
  public void close() throws Exception {
    connection.close();
  }

  @Override
  @SneakyThrows
  public boolean isClosed() {
    return connection.isClosed();
  }
}
