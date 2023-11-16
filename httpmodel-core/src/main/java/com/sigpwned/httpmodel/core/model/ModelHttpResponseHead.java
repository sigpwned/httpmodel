/*-
 * =================================LICENSE_START==================================
 * httpmodel-core
 * ====================================SECTION=====================================
 * Copyright (C) 2022 - 2023 Andy Boothe
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
import java.io.InputStream;
import com.sigpwned.httpmodel.core.util.ModelHttpStatusCodes;

public class ModelHttpResponseHead {
  public static ModelHttpResponseHead fromResponse(ModelHttpResponse response) {
    return new ModelHttpResponseHead(response.getStatusCode(), response.getHeaders());
  }

  /**
   * @see ModelHttpStatusCodes
   */
  private int statusCode;

  private ModelHttpHeaders headers;

  public ModelHttpResponseHead(int statusCode, ModelHttpHeaders headers) {
    if (headers == null)
      throw new NullPointerException();
    this.statusCode = statusCode;
    this.headers = headers;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public ModelHttpResponseHead setStatusCode(int statusCode) {
    this.statusCode = statusCode;
    return this;
  }

  public ModelHttpHeaders getHeaders() {
    return headers;
  }

  public ModelHttpResponseHead setHeaders(ModelHttpHeaders headers) {
    this.headers = headers;
    return this;
  }

  public ModelHttpResponseHeadBuilder toBuilder() {
    return new ModelHttpResponseHeadBuilder(this);
  }

  public ModelHttpResponse toResponse() throws IOException {
    return new ModelHttpResponse(this);
  }

  public ModelHttpResponse toResponse(ModelHttpEntity entity) throws IOException {
    return new ModelHttpResponse(this, entity);
  }

  public ModelHttpResponse toResponse(InputStream entity) throws IOException {
    return new ModelHttpResponse(this, entity);
  }

  @Override
  public String toString() {
    return "ModelHttpResponseHead [statusCode=" + statusCode + ", headers=" + headers + "]";
  }

}
