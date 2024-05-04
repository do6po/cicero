package org.do6po.cicero.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PredicateOperatorEnum {
  EQUALS("="),
  NOT_EQUALS("="),
  LIKE("LIKE"),
  ILIKE("ILIKE"),
  IN("IN"),
  NOT_IN("NOT IN"),
  GT(">"),
  GTE(">="),
  LT("<"),
  LTE("<=");

  private final String value;
}
