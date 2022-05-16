package com.sigpwned.httpmodel;

import java.util.Objects;

public class ModelHttpQueryParameter {
  public static ModelHttpQueryParameter of(String key, String value) {
    return new ModelHttpQueryParameter(key, value);
  }

  private final String key;
  private final String value;

  public ModelHttpQueryParameter(String key, String value) {
    if (key == null)
      throw new NullPointerException();
    if (value == null)
      throw new NullPointerException();
    this.key = key;
    this.value = value;
  }

  /**
   * @return the name
   */
  public String getKey() {
    return key;
  }

  /**
   * @return the value
   */
  public String getValue() {
    return value;
  }

  @Override
  public int hashCode() {
    return Objects.hash(key, value);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ModelHttpQueryParameter other = (ModelHttpQueryParameter) obj;
    return Objects.equals(key, other.key) && Objects.equals(value, other.value);
  }

  @Override
  public String toString() {
    return "ModelHttpHeader [name=" + key + ", value=" + value + "]";
  }
}
