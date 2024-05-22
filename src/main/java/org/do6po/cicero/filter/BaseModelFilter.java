package org.do6po.cicero.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.do6po.cicero.model.BaseModel;
import org.do6po.cicero.query.ModelQueryBuilder;

@Slf4j
public abstract class BaseModelFilter<M extends BaseModel<M, B>, B extends ModelQueryBuilder<M, B>>
    implements ModelFilter<M, B> {

  @Getter protected B builder;

  protected Map<String, Object> filterData = new HashMap<>();
  protected Map<String, Object> orderData = new HashMap<>();

  @Override
  public void fill(B builder) {
    this.builder = builder;

    applyFilterMethods();
    applyOrderMethods();
  }

  public BaseModelFilter<M, B> putFilterObject(Object value) {
    for (Field field : value.getClass().getDeclaredFields()) {
      try {
        field.setAccessible(true);

        filterData.put(field.getName(), field.get(value));
      } catch (IllegalAccessException e) {
        log.error("BaseModelFilter.putFilterObject error", e);
      }
    }

    return this;
  }

  public BaseModelFilter<M, B> clearFilterObject(Object value) {
    filterData.clear();
    return this;
  }

  public BaseModelFilter<M, B> putOrderObject(Object value) {

    return this;
  }

  public BaseModelFilter<M, B> clearOrderObject(Object value) {
    orderData.clear();
    return this;
  }

  protected void applyFilterMethods() {
    List<Method> methods =
        Arrays.stream(getClass().getMethods())
            .filter(m -> Objects.nonNull(m.getAnnotation(FilterMethod.class)))
            .sorted(
                (m1, m2) -> {
                  FilterMethod a1 = m1.getAnnotation(FilterMethod.class);
                  FilterMethod a2 = m2.getAnnotation(FilterMethod.class);

                  return Integer.compare(a1.order(), a2.order());
                })
            .toList();

    for (Method method : methods) {
      String methodName = method.getName();
      Object argument = filterData.get(methodName);
      if (Objects.nonNull(argument)) {
        try {
          method.invoke(this, argument);
        } catch (Exception e) {
          log.error(
              "BaseModelFilter.applyFilterMethods error for method {}, and argument {}",
              methodName,
              argument,
              e);
        }
      }
    }
  }

  protected void applyOrderMethods() {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface FilterMethod {
    String name() default "";

    int order() default 0;
  }

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface OrderMethod {
    String name() default "";

    int order() default 0;
  }
}
