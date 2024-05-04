package org.do6po.cicero.configuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SqlDriverEnum {
  MYSQL("mysql"),
  POSTGRESQL("postgresql");

  private final String type;
}
