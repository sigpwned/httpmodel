package com.sigpwned.httpmodel;

import static java.util.Collections.unmodifiableList;
import java.util.List;
import java.util.Objects;
import com.sigpwned.httpmodel.util.ModelHttpStatusCodes;

public class ModelHttpResponse {
  public static ModelHttpResponse of(int statusCode, List<ModelHttpHeader> headers,
      ModelHttpEntity entity) {
    return new ModelHttpResponse(statusCode, headers, entity);
  }

  /**
   * @see ModelHttpStatusCodes
   */
  private final int statusCode;

  private final List<ModelHttpHeader> headers;

  private final ModelHttpEntity entity;

  public ModelHttpResponse(int statusCode, List<ModelHttpHeader> headers, ModelHttpEntity entity) {
    if (headers == null)
      throw new NullPointerException();
    this.statusCode = statusCode;
    this.headers = unmodifiableList(headers);
    this.entity = entity;
  }

  /**
   * @return the statusCode
   */
  public int getStatusCode() {
    return statusCode;
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
    return Objects.hash(entity, headers, statusCode);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ModelHttpResponse other = (ModelHttpResponse) obj;
    return Objects.equals(entity, other.entity) && Objects.equals(headers, other.headers)
        && statusCode == other.statusCode;
  }

  @Override
  public String toString() {
    return "ModelHttpResponse [statusCode=" + statusCode + ", headers=" + headers + ", entity="
        + entity + "]";
  }
}
