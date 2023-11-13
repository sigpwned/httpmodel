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
import com.sigpwned.httpmodel.core.util.ModelHttpVersions;

/**
 * Models an HTTP request
 */
public class ModelHttpRequestBuilder {
  private String version;

  private String method;

  private ModelHttpUrl url;

  private ModelHttpHeaders headers;

  private ModelHttpEntity entity;

  public ModelHttpRequestBuilder() {
    this.version = ModelHttpVersions.DEFAULT;
  }

  public ModelHttpRequestBuilder(ModelHttpRequestBuilder that) {
    this.version = that.version;
    this.method = that.method;
    this.url = that.url;
    this.headers = that.headers;
    this.entity = that.entity;
  }

  public ModelHttpRequestBuilder(ModelHttpRequest that) {
    this.version = that.getVersion();
    this.method = that.getMethod();
    this.url = that.getUrl();
    this.headers = that.getHeaders();
    this.entity = that.getEntity().orElse(null);
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

  public ModelHttpUrl url() {
    return url;
  }

  public ModelHttpRequestBuilder url(ModelHttpUrl url) {
    this.url = url;
    return this;
  }

  public ModelHttpHeaders headers() {
    return headers;
  }

  public ModelHttpRequestBuilder headers(ModelHttpHeaders headers) {
    this.headers = headers;
    return this;
  }

  public ModelHttpEntity entity() {
    return entity;
  }

  public ModelHttpRequestBuilder entity(ModelHttpEntity entity) {
    this.entity = entity;
    return this;
  }

  public ModelHttpRequest build() {
    return new ModelHttpRequest(this);
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
    ModelHttpRequestBuilder other = (ModelHttpRequestBuilder) obj;
    return Objects.equals(entity, other.entity) && Objects.equals(headers, other.headers)
        && Objects.equals(method, other.method) && Objects.equals(url, other.url)
        && Objects.equals(version, other.version);
  }

  @Override
  public String toString() {
    return "ModelHttpRequestBuilder [version=" + version + ", method=" + method + ", url=" + url
        + ", headers=" + headers + ", entity=" + entity + "]";
  }
}
