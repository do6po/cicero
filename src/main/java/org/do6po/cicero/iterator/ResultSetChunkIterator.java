package org.do6po.cicero.iterator;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ResultSetChunkIterator<T> implements Iterator<List<T>> {

  private List<T> result;

  private final Supplier<List<T>> supplier;

  @Override
  public boolean hasNext() {
    result = supplier.get();
    return !result.isEmpty();
  }

  @Override
  public List<T> next() {
    if (Objects.isNull(result)) {
      return supplier.get();
    }

    List<T> current = result;
    result = null;
    return current;
  }
}
