package org.do6po.cicero.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OperatorEnum {
  AND("AND"),
  OR("OR");

  private final String value;
}
