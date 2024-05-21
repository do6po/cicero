package org.do6po.cicero.processor;

import static org.do6po.cicero.utils.DotUtil.dot;
import static org.do6po.cicero.utils.StringUtil.snakeToCamelCase;

import com.google.auto.service.AutoService;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import lombok.extern.slf4j.Slf4j;
import org.do6po.cicero.annotation.ModelProjection;
import org.do6po.cicero.relation.Relation;

@Slf4j
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@SupportedAnnotationTypes("org.do6po.cicero.annotation.ModelProjection")
public class ModelProjectionProcessor extends AbstractProcessor {

  public static final String PUBLIC_CONSTANT = "public static final String %s = \"%s\";\n";

  @Override
  public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
    set.forEach(a -> roundEnv.getElementsAnnotatedWith(a).forEach(this::generateTableNameGetter));

    return true;
  }

  private void generateTableNameGetter(Element element) {
    ModelProjection annotation = element.getAnnotation(ModelProjection.class);

    Element enclosingElement = element.getEnclosingElement();
    String packageName = enclosingElement.toString();
    String modelClassName = element.getSimpleName().toString();
    String modeRelationClass =
        annotation.projectionClassName().isEmpty()
            ? modelClassName + "_"
            : annotation.projectionClassName();

    String projectionClassNamespace = dot(packageName, modeRelationClass);

    try (PrintWriter writer =
        new PrintWriter(
            processingEnv.getFiler().createSourceFile(projectionClassNamespace).openWriter())) {

      writer.println(
          """
          package %s;

          public class %s {
          """
              .formatted(packageName, modeRelationClass));

      writer.println(PUBLIC_CONSTANT.formatted("table", annotation.table()));

      // Relations
      writer.println("public static class %s {".formatted(annotation.relationClassName()));
      extractRelations(element).forEach(r -> writer.println(PUBLIC_CONSTANT.formatted(r, r)));
      writer.println("}");

      // Columns
      writer.println("public static class %s {".formatted(annotation.columnClassName()));
      extractColumns(element)
          .forEach(r -> writer.println(PUBLIC_CONSTANT.formatted(snakeToCamelCase(r), r)));
      writer.println("}");

      writer.println("}");

    } catch (Exception e) {
      log.error("ModelProcessor.generateTableNameGetter failed!", e);
    }
  }

  private List<String> extractColumns(Element element) {
    return Arrays.stream(element.getAnnotation(ModelProjection.class).columns()).toList();
  }

  private List<String> extractRelations(Element element) {
    return element.getEnclosedElements().stream()
        .filter(e -> ElementKind.METHOD.equals(e.getKind()))
        .filter(
            m ->
                ((ExecutableElement) m)
                    .getReturnType()
                    .toString()
                    .startsWith(Relation.class.getCanonicalName()))
        .map(Element::getSimpleName)
        .map(Name::toString)
        .toList();
  }
}
