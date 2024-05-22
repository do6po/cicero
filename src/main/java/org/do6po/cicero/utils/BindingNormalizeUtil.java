package org.do6po.cicero.utils;

import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.do6po.cicero.binding.BindingNormalizer;
import org.do6po.cicero.binding.BindingNormalizerContainer;

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

    normalizers = BindingNormalizerContainer.getNormalizers();

    return normalizers;
  }

  private static Object normalizeBindings(Object binding) {
    for (BindingNormalizer normalizer : getNormalizers()) {
      if (normalizer.canNormalize(binding)) {
        return normalizer.normalize(binding);
      }
    }

    return binding;
  }
}
