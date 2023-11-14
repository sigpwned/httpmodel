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
import java.io.InputStream;
import java.util.Optional;
import com.sigpwned.httpmodel.core.io.ByteFilterSource;
import com.sigpwned.httpmodel.core.model.ModelHttpHeaders.Header;
import com.sigpwned.httpmodel.core.util.ModelHttpHeaderNames;
import com.sigpwned.httpmodel.core.util.ModelHttpStatusCodes;

/**
 * Models an HTTP response
 */
public class ModelHttpResponse extends ModelHttpEntityInputStream {
  public static ModelHttpResponseBuilder builder() {
    return new ModelHttpResponseBuilder();
  }

  /**
   * @see ModelHttpStatusCodes
   */
  private int statusCode;

  private ModelHttpHeaders headers;

  public ModelHttpResponse(int statusCode, ModelHttpHeaders headers, InputStream entity) {
    if (headers == null)
      throw new NullPointerException();
    this.statusCode = statusCode;
    this.headers = headers;
  }

  /* default */ ModelHttpResponse(ModelHttpResponseBuilder b, InputStream entity) {
    this(b.statusCode(), b.headers(), entity);
  }

  public int getStatusCode() {
    return statusCode;
  }

  public ModelHttpResponse setStatusCode(int statusCode) {
    this.statusCode = statusCode;
    return this;
  }

  public ModelHttpHeaders getHeaders() {
    return headers;
  }

  public ModelHttpResponse setHeaders(ModelHttpHeaders headers) {
    this.headers = headers;
    return this;
  }

  @Override
  public Optional<ModelHttpMediaType> getContentType() {
    if (!hasEntity())
      return Optional.empty();
    return this.getHeaders().findFirstHeaderByName(ModelHttpHeaderNames.CONTENT_TYPE)
        .map(Header::getValue).map(ModelHttpMediaType::fromString);
  }

  public ModelHttpResponse decode(ByteFilterSource filterSource) throws IOException {
    filter(filterSource);
    return this;
  }

  @Override
  public String toString() {
    return "ModelHttpResponse [statusCode=" + statusCode + ", headers=" + headers + "]";
  }
}
