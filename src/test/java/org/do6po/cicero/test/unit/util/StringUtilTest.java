package org.do6po.cicero.test.unit.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.do6po.cicero.utils.StringUtil;
import org.junit.jupiter.api.Test;

class StringUtilTest {
  @Test
  void camelCaseToSnakeCase() {
    assertEquals("camel_case_word", StringUtil.camelCaseToSnakeCase("camelCaseWord"));
  }

  @Test
  void snakeCaseToCamelCase() {
    assertEquals("camelCaseWord", StringUtil.snakeToCamelCase("camel_case_word"));
  }
}
