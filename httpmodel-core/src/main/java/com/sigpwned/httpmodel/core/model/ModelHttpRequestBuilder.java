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
import com.sigpwned.httpmodel.core.util.ModelHttpHeaderNames;
import com.sigpwned.httpmodel.core.util.ModelHttpVersions;

/**
 * Models an HTTP request
 */
public class ModelHttpRequestBuilder {
  private String version;

  private String method;

  private ModelHttpUrlBuilder url;

  private ModelHttpHeadersBuilder headers;

  public ModelHttpRequestBuilder() {
    this.version = ModelHttpVersions.DEFAULT;
  }

  public ModelHttpRequestBuilder(ModelHttpRequestBuilder that) {
    this.version = that.version;
    this.method = that.method;
    this.url = that.url;
    this.headers = that.headers;
  }

  public ModelHttpRequestBuilder(ModelHttpRequest that) {
    this.version = that.getVersion();
    this.method = that.getMethod();
    this.url = new ModelHttpUrlBuilder(that.getUrl());
    this.headers = new ModelHttpHeadersBuilder(that.getHeaders());
  }

  public String version() {
    return version;
  }

  public ModelHttpRequestBuilder version(String version) {
    this.version = version;
    return this;
  }

  public String method() {
    return method;
  }

  public ModelHttpRequestBuilder method(String method) {
    this.method = method;
    return this;
  }

  public ModelHttpUrlBuilder url() {
    return url;
  }

  public ModelHttpHeadersBuilder headers() {
    return headers;
  }

  public ModelHttpRequest build(ModelHttpEntity entity) throws IOException {
    ModelHttpRequest result = null;
    ModelHttpEntityInputStream stream = entity.toEntityInputStream();
    try {
      headers().setOnlyHeader(ModelHttpHeaderNames.CONTENT_TYPE, null);
      result = new ModelHttpRequest(this, stream);
    } finally {
      if (result == null)
        stream.close();
    }
    return result;
  }

  public ModelHttpRequest build(InputStream entity) {
    return new ModelHttpRequest(this, entity);
  }
}
