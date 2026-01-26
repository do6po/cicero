package org.do6po.cicero.test.integration.query;

import static com.github.darrmirr.dbchange.annotation.onclass.DbChangeOnce.ExecutionPhase.AFTER_ALL;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.darrmirr.dbchange.annotation.SqlExecutorGetter;
import com.github.darrmirr.dbchange.annotation.onclass.DbChangeOnce;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.do6po.cicero.iterator.ChunkIterator;
import org.do6po.cicero.test.integration.BaseDbTest;
import org.do6po.cicero.test.integration.model.UserM;
import org.junit.jupiter.api.Test;

@SqlExecutorGetter(BaseDbTest.DEFAULT_SQL_EXECUTOR)
@DbChangeOnce(sqlQueryFiles = "sql/users__up.sql")
@DbChangeOnce(sqlQueryFiles = "sql/users__down.sql", executionPhase = AFTER_ALL)
class ChunkTest extends BaseDbTest {

  @Test
  void chunk_iterator_processes_all_records() {
    List<UserM> allUsers = new ArrayList<>();

    try (ChunkIterator<UserM, ?> iterator = userQuery().chunk(2)) {
      while (iterator.hasNext()) {
        allUsers.addAll(iterator.next());
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    assertEquals(6, allUsers.size());
  }

  @Test
  void chunk_consumer_processes_all_records() throws SQLException {
    List<UserM> allUsers = new ArrayList<>();

    userQuery().chunk(2, allUsers::addAll);

    assertEquals(6, allUsers.size());
  }

  @Test
  void chunk_consumer_default_size() throws SQLException {
    List<UserM> allUsers = new ArrayList<>();

    userQuery().chunk(allUsers::addAll);

    assertEquals(6, allUsers.size());
  }

  @Test
  void chunk_iterator_correct_chunk_sizes() {
    List<Integer> chunkSizes = new ArrayList<>();

    try (ChunkIterator<UserM, ?> iterator = userQuery().chunk(2)) {
      while (iterator.hasNext()) {
        chunkSizes.add(iterator.next().size());
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    assertEquals(List.of(2, 2, 2), chunkSizes);
  }

  @Test
  void chunk_consumer_counts_chunks() throws SQLException {
    AtomicInteger chunkCount = new AtomicInteger(0);

    userQuery().chunk(2, users -> chunkCount.incrementAndGet());

    assertEquals(3, chunkCount.get());
  }
}
