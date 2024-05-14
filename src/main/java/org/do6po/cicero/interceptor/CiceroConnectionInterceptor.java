package org.do6po.cicero.interceptor;

public interface CiceroConnectionInterceptor {
  void startQueryCount();

  void stopQueryCount();

  Long getQueryCount();
}
