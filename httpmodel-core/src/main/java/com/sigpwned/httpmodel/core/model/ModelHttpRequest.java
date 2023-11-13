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
import java.util.Optional;
import com.sigpwned.httpmodel.core.model.ModelHttpHeaders.Header;
import com.sigpwned.httpmodel.core.util.ModelHttpHeaderNames;
import com.sigpwned.httpmodel.core.util.ModelHttpMethods;
import com.sigpwned.httpmodel.core.util.ModelHttpVersions;

/**
 * Models an HTTP request
 */
public class ModelHttpRequest extends ModelHttpEntityInputStream {
  public static ModelHttpRequestBuilder builder() {
    return new ModelHttpRequestBuilder();
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

  private final ModelHttpHeaders headers;

  public ModelHttpRequest(String version, String method, ModelHttpUrl url, ModelHttpHeaders headers,
      InputStream entity) {
    super(entity);
    if (version == null)
      throw new NullPointerException();
    if (method == null)
      throw new NullPointerException();
    if (url == null)
      throw new NullPointerException();
    if (headers == null)
      throw new NullPointerException();
    this.version = version.toUpperCase();
    this.method = method.toUpperCase();
    this.url = url;
    this.headers = headers;
  }

  /* default */ ModelHttpRequest(ModelHttpRequestBuilder b, InputStream entity) {
    this(b.version(), b.method(), b.url(), b.headers(), entity);
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
  public ModelHttpHeaders getHeaders() {
    return headers;
  }

  @Override
  public Optional<ModelHttpMediaType> getContentType() {
    if (!hasEntity())
      return Optional.empty();
    return getHeaders().findFirstHeaderByName(ModelHttpHeaderNames.CONTENT_TYPE)
        .map(Header::getValue).map(ModelHttpMediaType::fromString);
  }

  public ModelHttpRequestBuilder toBuilder() {
    return new ModelHttpRequestBuilder(this);
  }

  @Override
  public String toString() {
    return "ModelHttpRequest [version=" + version + ", method=" + method + ", url=" + url
        + ", headers=" + headers + "]";
  }
}
