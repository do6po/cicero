package org.do6po.cicero.interceptor;

public interface ConnectionInterceptor {
  void startQueryCount();

  void stopQueryCount();

  Long getQueryCount();
}
