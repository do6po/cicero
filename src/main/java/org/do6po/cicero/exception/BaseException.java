package org.do6po.cicero.exception;

public class BaseException extends RuntimeException {

  public BaseException(String message, Throwable cause) {
    super(message, cause);
  }

  public BaseException(String message) {
    super(message);
  }
}
