package org.do6po.cicero.iterator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.Getter;
import lombok.SneakyThrows;
import org.do6po.cicero.configuration.DbDriver;
import org.do6po.cicero.query.Builder;
import org.do6po.cicero.utils.ResultSetUtil;

/**
 * Iterator implementation for chunked processing of database query results.
 *
 * <p>This iterator uses a peek-ahead pattern to efficiently determine if more data is available
 * without relying on {@link ResultSet#isAfterLast()}, which doesn't work reliably with PostgreSQL
 * cursors.
 *
 * @param <T> the entity type
 * @param <B> the builder type
 */
public class PlantChunkIterator<T, B extends Builder<T, B>> implements ChunkIterator<T, B> {

  @Getter protected final B builder;
  protected final int size;
  protected final Connection connection;
  protected final ResultSet resultSet;

  private List<T> nextChunk;
  private boolean fetchedNext = false;

  public PlantChunkIterator(B builder, int size) throws SQLException {
    this.builder = builder;
    this.size = size;

    DbDriver dbDriver = builder.getDbDriver();
    this.connection = dbDriver.getConnection();
    this.resultSet =
        dbDriver.createCursor(connection, builder.getSqlExpression(), cs -> cs.setFetchSize(size));
  }

  /**
   * Returns {@code true} if the iteration has more chunks.
   *
   * <p>This method uses peek-ahead to determine if more data is available by attempting to fetch
   * the next chunk without consuming it.
   *
   * @return {@code true} if the iterator has more chunks
   */
  @Override
  public boolean hasNext() {
    if (!fetchedNext) {
      nextChunk = fetchNextChunk();
      fetchedNext = true;
    }

    return nextChunk != null && !nextChunk.isEmpty();
  }

  /**
   * Returns the next chunk of results.
   *
   * @return the next chunk of results
   * @throws NoSuchElementException if no more chunks are available
   */
  @Override
  public List<T> next() {
    if (!hasNext()) {
      throw new NoSuchElementException("No more chunks available");
    }

    List<T> result = nextChunk;
    nextChunk = null;
    fetchedNext = false;

    return result;
  }

  /**
   * Fetches the next chunk of data from the ResultSet.
   *
   * @return the next chunk of results, or empty list if no more data available
   */
  private List<T> fetchNextChunk() {
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
