package org.do6po.cicero.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UnionTypeEnum {
  UNION("UNION"),
  UNION_ALL("UNION ALL");

  private final String value;
}
