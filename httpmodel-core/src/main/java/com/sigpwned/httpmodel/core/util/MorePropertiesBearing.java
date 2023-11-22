package com.sigpwned.httpmodel.core.util;

import java.util.Map;
import java.util.stream.Collectors;
import com.sigpwned.httpmodel.core.model.PropertiesBearing;

public final class MorePropertiesBearing {
  private MorePropertiesBearing() {}

  public static Map<String, Object> toMap(PropertiesBearing props) {
    return props.getPropertyNames().stream()
        .collect(Collectors.toMap(s -> s, s -> props.getProperty(s).get()));
  }
}
