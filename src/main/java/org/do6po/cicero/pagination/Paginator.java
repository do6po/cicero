package org.do6po.cicero.pagination;

import java.util.Collection;

public interface Paginator<T> {
  Collection<T> getItems();

  int getPage();

  int getPageSize();

  long getTotalCount();

  boolean isFirst();

  boolean isLast();

  boolean isEmpty();

  boolean hasNext();

  boolean hasPrevious();
}
