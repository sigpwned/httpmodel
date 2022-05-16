package com.sigpwned.httpmodel;

import static java.util.Collections.unmodifiableList;
import java.util.List;
import java.util.Objects;

public class ModelHttpUrl {
  public static ModelHttpUrl of(String path, List<ModelHttpQueryParameter> parameters) {
    return new ModelHttpUrl(path, parameters);
  }

  private final String path;
  
  private final List<ModelHttpQueryParameter> parameters;

  public ModelHttpUrl(String path, List<ModelHttpQueryParameter> parameters) {
    if (path == null)
      throw new NullPointerException();
    if (parameters == null)
      throw new NullPointerException();
    this.path = path;
    this.parameters = unmodifiableList(parameters);
  }

  /**
   * @return the path
   */
  public String getPath() {
    return path;
  }

  /**
   * @return the parameters
   */
  public List<ModelHttpQueryParameter> getParameters() {
    return parameters;
  }

  @Override
  public int hashCode() {
    return Objects.hash(parameters, path);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ModelHttpUrl other = (ModelHttpUrl) obj;
    return Objects.equals(parameters, other.parameters) && Objects.equals(path, other.path);
  }

  @Override
  public String toString() {
    return "ModelHttpUrl [path=" + path + ", parameters=" + parameters + "]";
  }
}
