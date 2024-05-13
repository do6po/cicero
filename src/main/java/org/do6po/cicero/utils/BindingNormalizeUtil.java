package org.do6po.cicero.utils;

import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.do6po.cicero.binding.BindingNormalizer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BindingNormalizeUtil {

  private static List<BindingNormalizer> normalizers;

  public static Object normalize(Object value) {
    return normalizeBindings(value);
  }

  public static List<BindingNormalizer> getNormalizers() {
    if (Objects.nonNull(normalizers)) {
      return normalizers;
    }

    return List.of();
  }

  private static Object normalizeBindings(Object binding) {
    if (binding instanceof Enum<?> value) {
      return value.name();
    }

    if (binding instanceof Class<?> bClass) {
      return bClass.getCanonicalName();
    }

    return binding;
  }
}
