package org.do6po.cicero.binding;

public interface BindingNormalizer {
  boolean canNormalize(Object type);

  Object normalize(Object type);
}
