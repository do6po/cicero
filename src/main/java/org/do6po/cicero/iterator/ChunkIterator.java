package org.do6po.cicero.iterator;

import java.util.Iterator;
import java.util.List;
import org.do6po.cicero.query.Builder;

public interface ChunkIterator<T, B extends Builder<T, B>>
    extends Iterator<List<T>>, AutoCloseable {
  B getBuilder();

  boolean isClosed();
}
