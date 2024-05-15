package org.do6po.cicero.configuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SqlDriverEnum {
  POSTGRESQL("postgresql");

  private final String type;
}
