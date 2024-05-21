package org.do6po.cicero.binding;

public class ClassBindingNormalizer implements BindingNormalizer {

  @Override
  public boolean canNormalize(Object type) {
    return type instanceof Class<?>;
  }

  @Override
  public Object normalize(Object type) {
    if (type instanceof Class<?> value) {
      return value.getCanonicalName();
    }

    throw getBindingNormalizeException(type.getClass());
  }
}
