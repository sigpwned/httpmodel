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

import java.io.InputStream;

/**
 * Models an HTTP response
 */
public class ModelHttpResponseBuilder {
  private int statusCode;

  private ModelHttpHeaders headers;

  public ModelHttpResponseBuilder() {}

  public ModelHttpResponseBuilder(ModelHttpResponseBuilder that) {
    if (that == null)
      throw new NullPointerException();
    this.statusCode = that.statusCode;
    this.headers = that.headers;
  }

  public ModelHttpResponseBuilder(ModelHttpResponse that) {
    if (that == null)
      throw new NullPointerException();
    this.statusCode = that.getStatusCode();
    this.headers = that.getHeaders();
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

  public ModelHttpResponse build(InputStream entity) {
    return new ModelHttpResponse(this, entity);
  }

  @Override
  public String toString() {
    return "ModelHttpResponseBuilder [statusCode=" + statusCode + ", headers=" + headers + "]";
  }
}
