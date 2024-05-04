package org.do6po.cicero.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DirectionEnum {
  ASC("ASC"),
  DESC("DESC");

  private final String value;
}
