package com.sigpwned.httpmodel;

import java.util.List;
import java.util.Objects;
import com.sigpwned.httpmodel.util.ModelHttpMethods;
import com.sigpwned.httpmodel.util.ModelHttpVersions;

public class ModelHttpRequest {
  public static ModelHttpRequest of(String version, String method, ModelHttpUrl url,
      List<ModelHttpHeader> headers, ModelHttpEntity entity) {
    return new ModelHttpRequest(version, method, url, headers, entity);
  }

  /**
   * @see ModelHttpVersions
   */
  private final String version;

  /**
   * @see ModelHttpMethods
   */
  private final String method;

  private final ModelHttpUrl url;

  private final List<ModelHttpHeader> headers;

  private final ModelHttpEntity entity;

  public ModelHttpRequest(String version, String method, ModelHttpUrl url,
      List<ModelHttpHeader> headers, ModelHttpEntity entity) {
    if (version == null)
      throw new NullPointerException();
    if (method == null)
      throw new NullPointerException();
    if (url == null)
      throw new NullPointerException();
    if (headers == null)
      throw new NullPointerException();
    this.version = version;
    this.method = method;
    this.url = url;
    this.headers = headers;
    this.entity = entity;
  }

  /**
   * @return the version
   */
  public String getVersion() {
    return version;
  }

  /**
   * @return the method
   */
  public String getMethod() {
    return method;
  }

  /**
   * @return the url
   */
  public ModelHttpUrl getUrl() {
    return url;
  }

  /**
   * @return the headers
   */
  public List<ModelHttpHeader> getHeaders() {
    return headers;
  }

  /**
   * @return the entity
   */
  public ModelHttpEntity getEntity() {
    return entity;
  }

  @Override
  public int hashCode() {
    return Objects.hash(entity, headers, method, url, version);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ModelHttpRequest other = (ModelHttpRequest) obj;
    return Objects.equals(entity, other.entity) && Objects.equals(headers, other.headers)
        && Objects.equals(method, other.method) && Objects.equals(url, other.url)
        && Objects.equals(version, other.version);
  }

  @Override
  public String toString() {
    return "ModelHttpRequest [version=" + version + ", method=" + method + ", url=" + url
        + ", headers=" + headers + ", entity=" + entity + "]";
  }
}
