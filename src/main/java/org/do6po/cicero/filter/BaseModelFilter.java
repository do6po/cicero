package org.do6po.cicero.filter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.do6po.cicero.annotation.FilterMethod;
import org.do6po.cicero.model.BaseModel;
import org.do6po.cicero.query.ModelQueryBuilder;

@Slf4j
@Getter
public abstract class BaseModelFilter<M extends BaseModel<M, B>, B extends ModelQueryBuilder<M, B>>
    implements ModelFilter<M, B> {

  protected B builder;
  protected Map<String, Object> filterData = new HashMap<>();

  @Override
  public void fill(B builder) {
    this.builder = builder;

    applyFilterMethods();
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
}
