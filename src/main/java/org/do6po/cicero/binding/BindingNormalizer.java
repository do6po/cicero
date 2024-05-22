package org.do6po.cicero.binding;

public interface BindingNormalizer {
  boolean canNormalize(Object type);

  Object normalize(Object type);

  default BindingNormalizeException getBindingNormalizeException(Class<?> aClass) {
    return new BindingNormalizeException("Can't normalize binding object %s".formatted(aClass));
  }
}
