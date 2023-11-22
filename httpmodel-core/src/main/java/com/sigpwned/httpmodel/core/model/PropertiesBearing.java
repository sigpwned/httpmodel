package com.sigpwned.httpmodel.core.model;

import java.util.Optional;
import java.util.Set;

public interface PropertiesBearing {
  public Optional<Object> getProperty(String name);

  public Set<String> getPropertyNames();

  public void setProperty(String name, Object value);

  default void removeProperty(String name) {
    setProperty(name, null);
  }
}
