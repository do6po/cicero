package org.do6po.cicero.utils;

import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtil {

  public static String camelCaseToSnakeCase(String str) {
    String regex = "([a-z])([A-Z]+)";
    String replacement = "$1_$2";

    return str.replaceAll(regex, replacement).toLowerCase();
  }

  public static String snakeToCamelCase(String str) {
    return lcFirst(
        Arrays.stream(str.split("_")).map(StringUtil::ucFirst).collect(Collectors.joining()));
  }

  public static String ucFirst(String word) {
    return word.substring(0, 1).toUpperCase() + word.substring(1);
  }

  public static String lcFirst(String word) {
    return word.substring(0, 1).toLowerCase() + word.substring(1);
  }
}
