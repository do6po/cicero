package org.do6po.cicero.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.do6po.cicero.exception.ExtractRelationException;
import org.do6po.cicero.model.BaseModel;
import org.do6po.cicero.query.ModelQueryBuilder;
import org.do6po.cicero.relation.Relation;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RelationUtil {

  @SuppressWarnings("unchecked")
  public static <
          M extends BaseModel<M, ?>,
          F extends BaseModel<F, Q>,
          Q extends ModelQueryBuilder<F, Q>,
          R>
      Relation<M, F, Q, R> extractRelation(
          @NonNull BaseModel<M, ?> baseModel, @NonNull String relation) {

    try {
      Method method = baseModel.getClass().getMethod(relation);

      return (Relation<M, F, Q, R>) method.invoke(baseModel);
    } catch (NoSuchMethodException e) {
      String message =
          "There are no such method '%s' with relation configuration in the class '%s'"
              .formatted(relation, baseModel.getClass().getName());

      log.error("BaseModel.getRelationConfig: {}", message, e);

      throw new ExtractRelationException(message, e);
    } catch (IllegalAccessException | InvocationTargetException e) {
      String message = "Extract relation error!";

      log.error("BaseModel.getRelationConfig: {}", message, e);

      throw new ExtractRelationException(message, e);
    }
  }

  public static <M extends BaseModel<M, ?>> Collection<String> extractRelationMethodNames(
      M currentModel) {
    return Arrays.stream(currentModel.getClass().getDeclaredMethods())
        .filter(m -> m.getReturnType().equals(Relation.class))
        .map(Method::getName)
        .toList();
  }
}
