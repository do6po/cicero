package org.do6po.cicero.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) // Use the annotation on the method.
@Retention(RetentionPolicy.SOURCE)
@Inherited
public @interface ModelProjection {
  String table();

  String projectionClassName() default "";

  String columnClassName() default "Columns";

  String[] columns() default {};

  String relationClassName() default "Relations";
}
