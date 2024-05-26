package org.do6po.cicero.interceptor;

public interface QueryCounter {
  void start();

  void tryIncrement();

  void stop();

  Long get();
}
