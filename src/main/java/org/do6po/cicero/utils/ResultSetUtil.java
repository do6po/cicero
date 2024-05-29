package org.do6po.cicero.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResultSetUtil {

  @SneakyThrows(SQLException.class)
  public static @NonNull <T> List<T> mapList(
      ResultSet resultSet, Function<ResultSet, T> function, Integer chunk) {
    List<T> result = new ArrayList<>();
    while (resultSet.next()) {
      result.add(function.apply(resultSet));

      if (Objects.nonNull(chunk) && result.size() == chunk) {
        return result;
      }
    }

    return result;
  }
}
