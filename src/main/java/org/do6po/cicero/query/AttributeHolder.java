package org.do6po.cicero.query;

import java.util.LinkedHashMap;
import lombok.Getter;

@Getter
public class AttributeHolder {
  private final LinkedHashMap<String, Object> attributes = new LinkedHashMap<>();

  public static AttributeHolder attrs() {
    return new AttributeHolder();
  }

  public AttributeHolder put(String column, Object value) {
    attributes.put(column, value);

    return this;
  }
}
