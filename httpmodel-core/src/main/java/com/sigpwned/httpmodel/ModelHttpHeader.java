package com.sigpwned.httpmodel;

import java.util.Objects;

public class ModelHttpHeader {
  public static ModelHttpHeader of(String name, String value) {
    return new ModelHttpHeader(name, value);
  }

  private final String name;
  private final String value;

  public ModelHttpHeader(String name, String value) {
    if (name == null)
      throw new NullPointerException();
    if (value == null)
      throw new NullPointerException();
    this.name = name;
    this.value = value;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @return the value
   */
  public String getValue() {
    return value;
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, value);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ModelHttpHeader other = (ModelHttpHeader) obj;
    return Objects.equals(name, other.name) && Objects.equals(value, other.value);
  }

  @Override
  public String toString() {
    return "ModelHttpHeader [name=" + name + ", value=" + value + "]";
  }
}
