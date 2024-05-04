package org.do6po.cicero.pagination;

import java.util.Collection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SimplePaginator<T> implements Paginator<T> {

  private final int page;
  private final int pageSize;
  private final long totalCount;
  private final Collection<T> items;

  @Override
  public boolean isFirst() {
    return getPage() == 1;
  }

  @Override
  public boolean isLast() {
    return !hasNext();
  }

  @Override
  public boolean isEmpty() {
    return items.isEmpty();
  }

  @Override
  public boolean hasNext() {
    return (long) page * pageSize <= totalCount;
  }

  @Override
  public boolean hasPrevious() {
    return getPage() > 1;
  }
}
