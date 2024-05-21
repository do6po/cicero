package org.do6po.cicero.test.integration.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.do6po.cicero.binding.BindingNormalizer;
import org.do6po.cicero.binding.BindingNormalizerContainer;
import org.do6po.cicero.exception.BaseException;
import org.do6po.cicero.query.QueryBuilder;
import org.do6po.cicero.test.integration.BaseDbTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

class BindingNormalizeUtilTest extends BaseDbTest {

  public static final String BINDING_EXCEPTION = "Binding exception!";
  private static final ThrowableBindingNormalizer THROWABLE_BINDING_NORMALIZER =
      new ThrowableBindingNormalizer();

  @AfterAll
  static void cleanUp() {
    BindingNormalizerContainer.getNormalizers().remove(THROWABLE_BINDING_NORMALIZER);
  }

  @Test
  void addCustomBindingNormalizer() {
    BindingNormalizerContainer.add(THROWABLE_BINDING_NORMALIZER);

    TestBindingException testBindingException =
        assertThrows(
            TestBindingException.class,
            () ->
                QueryBuilder.query("someTable").where("someColumn", new TargetBindingType()).get());

    assertEquals(BINDING_EXCEPTION, testBindingException.getMessage());
  }

  static class TargetBindingType {}

  static class ThrowableBindingNormalizer implements BindingNormalizer {

    @Override
    public boolean canNormalize(Object type) {
      return type instanceof TargetBindingType;
    }

    @Override
    public Object normalize(Object type) {
      throw new TestBindingException(BINDING_EXCEPTION);
    }
  }

  static class TestBindingException extends BaseException {

    public TestBindingException(String message) {
      super(message);
    }
  }
}
