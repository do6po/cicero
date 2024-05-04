package org.do6po.cicero.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StackUtil {
  public static final int CURRENT_LEVEL = 2;

  public static String getCallerMethodName(int level) {
    return Thread.currentThread().getStackTrace()[level + CURRENT_LEVEL].getMethodName();
  }
}
