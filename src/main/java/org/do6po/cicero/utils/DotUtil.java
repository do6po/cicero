package org.do6po.cicero.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.modelmapper.internal.Pair;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DotUtil {

  public static String dot(String... literals) {
    return String.join(".", literals);
  }

  public static Pair<String, String> cut(@NonNull String chain) {
    String first = chain.split("\\.")[0];
    if (first.length() == chain.length()) {
      return Pair.of(first, null);
    }

    String other = chain.replace(first + ".", "");

    return Pair.of(first, other);
  }
}
