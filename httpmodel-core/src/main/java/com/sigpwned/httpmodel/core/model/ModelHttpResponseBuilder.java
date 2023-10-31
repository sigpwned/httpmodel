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

import java.util.Objects;

/**
 * Models an HTTP response
 */
public class ModelHttpResponseBuilder {
  private int statusCode;

  private ModelHttpHeaders headers;

  private ModelHttpEntity entity;

  public ModelHttpResponseBuilder() {}

  public ModelHttpResponseBuilder(ModelHttpResponseBuilder that) {
    if (that == null)
      throw new NullPointerException();
    this.statusCode = that.statusCode;
    this.headers = that.headers;
    this.entity = that.entity;
  }

  public ModelHttpResponseBuilder(ModelHttpResponse that) {
    if (that == null)
      throw new NullPointerException();
    this.statusCode = that.getStatusCode();
    this.headers = that.getHeaders();
    this.entity = that.getEntity().orElse(null);
  }

  public int statusCode() {
    return statusCode;
  }

  public ModelHttpResponseBuilder statusCode(int statusCode) {
    this.statusCode = statusCode;
    return this;
  }

  public ModelHttpHeaders headers() {
    return headers;
  }

  public ModelHttpResponseBuilder headers(ModelHttpHeaders headers) {
    this.headers = headers;
    return this;
  }

  public ModelHttpEntity entity() {
    return entity;
  }

  public ModelHttpResponseBuilder entity(ModelHttpEntity entity) {
    this.entity = entity;
    return this;
  }

  public ModelHttpResponse build() {
    return new ModelHttpResponse(this);
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
    ModelHttpResponseBuilder other = (ModelHttpResponseBuilder) obj;
    return Objects.equals(entity, other.entity) && Objects.equals(headers, other.headers)
        && statusCode == other.statusCode;
  }

  @Override
  public String toString() {
    return "ModelHttpResponseBuilder [statusCode=" + statusCode + ", headers=" + headers
        + ", entity=" + entity + "]";
  }
}
