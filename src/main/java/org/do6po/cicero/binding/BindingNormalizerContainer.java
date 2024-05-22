package org.do6po.cicero.binding;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BindingNormalizerContainer {
  @Getter private static final List<BindingNormalizer> normalizers;

  static {
    normalizers = new ArrayList<>();

    add(new EnumBindingNormalizer());
    add(new ClassBindingNormalizer());
  }

  public static void add(BindingNormalizer normalizer) {
    normalizers.add(normalizer);
  }

  public static void clear() {
    normalizers.clear();
  }
}
