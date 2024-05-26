package org.do6po.cicero.interceptor;

public class CiceroQueryCounter implements QueryCounter {
  private final ThreadLocal<Long> threadQueryCount = new ThreadLocal<Long>();

  public void start() {
    threadQueryCount.set(0L);
  }

  @Override
  public synchronized void tryIncrement() {
    Long count = threadQueryCount.get();
    if (count != null) {
      threadQueryCount.set(count + 1);
    }
  }

  public void stop() {
    threadQueryCount.remove();
  }

  public Long get() {
    return threadQueryCount.get();
  }
}
