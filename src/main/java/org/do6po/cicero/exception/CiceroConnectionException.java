package org.do6po.cicero.exception;

public class CiceroConnectionException extends RuntimeException {

  public CiceroConnectionException(String message) {
    super(message);
  }

  public CiceroConnectionException(String message, Throwable cause) {
    super(message, cause);
  }
}
