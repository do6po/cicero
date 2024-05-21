package org.do6po.cicero.binding;

public class EnumBindingNormalizer implements BindingNormalizer {

  @Override
  public boolean canNormalize(Object type) {
    return type instanceof Enum<?>;
  }

  @Override
  public Object normalize(Object type) {
    if (type instanceof Enum<?> value) {
      return value.name();
    }

    throw getBindingNormalizeException(type.getClass());
  }
}
