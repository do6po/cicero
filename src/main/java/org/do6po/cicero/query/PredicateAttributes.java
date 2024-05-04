package org.do6po.cicero.query;

import java.util.LinkedHashMap;
import lombok.Getter;

@Getter
public class PredicateAttributes {
  private final LinkedHashMap<String, Object> attributes = new LinkedHashMap<>();

  public PredicateAttributes put(String column, Object value) {
    attributes.put(column, value);

    return this;
  }
}
