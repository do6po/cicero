package org.do6po.cicero.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtil {

  public static String camelCaseToSnakeCase(String str) {
    String regex = "([a-z])([A-Z]+)";
    String replacement = "$1_$2";

    return str.replaceAll(regex, replacement).toLowerCase();
  }
}
