/*-
 * =================================LICENSE_START==================================
 * httpmodel-core
 * ====================================SECTION=====================================
 * Copyright (C) 2022 Andy Boothe
 * ====================================SECTION=====================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ==================================LICENSE_END===================================
 */
package com.sigpwned.httpmodel.core.model;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import com.sigpwned.httpmodel.core.util.ModelHttpStatusCodes;

/**
 * Models an HTTP response
 */
public class ModelHttpResponse implements AutoCloseable {
  public static ModelHttpResponseBuilder builder() {
    return new ModelHttpResponseBuilder();
  }

  public static ModelHttpResponse of(int statusCode, ModelHttpEntity entity) {
    return of(statusCode, ModelHttpHeaders.of(), entity);
  }

  public static ModelHttpResponse of(int statusCode, ModelHttpHeaders headers,
      ModelHttpEntity entity) {
    return new ModelHttpResponse(statusCode, headers, entity);
  }

  public static ModelHttpResponse of(int statusCode, ModelHttpHeaders headers,
      Optional<ModelHttpEntity> entity) {
    return new ModelHttpResponse(statusCode, headers, entity.orElse(null));
  }

  /**
   * @see ModelHttpStatusCodes
   */
  private final int statusCode;

  private final ModelHttpHeaders headers;

  private final ModelHttpEntity entity;

  public ModelHttpResponse(int statusCode, ModelHttpHeaders headers, ModelHttpEntity entity) {
    if (headers == null)
      throw new NullPointerException();
    this.statusCode = statusCode;
    this.headers = headers;
    this.entity = entity;
  }

  /* default */ ModelHttpResponse(ModelHttpResponseBuilder that) {
    this(that.statusCode(), that.headers(), that.entity());
  }

  /**
   * @return the statusCode
   */
  public int getStatusCode() {
    return statusCode;
  }

  public ModelHttpResponse withStatusCode(int newStatusCode) {
    return toBuilder().statusCode(newStatusCode).build();
  }

  /**
   * @return the headers
   */
  public ModelHttpHeaders getHeaders() {
    return headers;
  }

  public ModelHttpResponse withHeaders(ModelHttpHeaders newHeaders) {
    return toBuilder().headers(newHeaders).build();
  }

  /**
   * @return the entity
   */
  public Optional<ModelHttpEntity> getEntity() {
    return Optional.ofNullable(entity);
  }

  public ModelHttpResponse withEntity(ModelHttpEntity newEntity) {
    return toBuilder().entity(newEntity).build();
  }

  @Override
  public void close() throws IOException {
    if (getEntity().isPresent())
      getEntity().get().close();
  }

  public ModelHttpResponseBuilder toBuilder() {
    return new ModelHttpResponseBuilder(this);
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
