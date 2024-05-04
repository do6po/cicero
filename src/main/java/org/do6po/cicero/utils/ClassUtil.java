package org.do6po.cicero.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClassUtil {

  public static <T> Class<T> guessType(Class<?> clazz, int index) {
    Type genericSuperclass = clazz.getGenericSuperclass();
    return (Class<T>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[index];
  }

  @SneakyThrows
  public static <T> T getInstance(Class<T> clazz) {
    return clazz.getConstructor().newInstance();
  }
}
