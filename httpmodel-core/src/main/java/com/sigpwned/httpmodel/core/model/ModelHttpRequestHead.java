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

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableSet;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import com.sigpwned.httpmodel.core.util.ModelHttpMethods;
import com.sigpwned.httpmodel.core.util.ModelHttpVersions;

public class ModelHttpRequestHead implements PropertiesBearing {
  public static ModelHttpRequestHeadBuilder builder() {
    return new ModelHttpRequestHeadBuilder();
  }

  public static ModelHttpRequestHead fromRequest(ModelHttpRequest request) {
    return new ModelHttpRequestHead(request.getVersion(), request.getMethod(), request.getUrl(),
        request.getHeaders(), request.getProperties());
  }

  /**
   * @see ModelHttpVersions
   */
  private String version;

  /**
   * @see ModelHttpMethods
   */
  private String method;

  private ModelHttpUrl url;

  private ModelHttpHeaders headers;

  /**
   * Not related to HTTP. Used to store arbitrary state during processing.
   */
  private Map<String, Object> properties;

  public ModelHttpRequestHead(String version, String method, ModelHttpUrl url,
      ModelHttpHeaders headers) {
    this(version, method, url, headers, emptyMap());
  }

  public ModelHttpRequestHead(String version, String method, ModelHttpUrl url,
      ModelHttpHeaders headers, Map<String, Object> properties) {
    if (version == null)
      throw new NullPointerException();
    if (method == null)
      throw new NullPointerException();
    if (url == null)
      throw new NullPointerException();
    if (headers == null)
      throw new NullPointerException();
    if (properties == null)
      throw new NullPointerException();
    this.version = version;
    this.method = method;
    this.url = url;
    this.headers = headers;
    this.properties = new HashMap<>(properties);
  }

  /* default */ ModelHttpRequestHead(ModelHttpRequestHeadBuilder b) {
    this(b.version(), b.method(), b.url().build(), b.headers().build(), b.properties());
  }

  public String getVersion() {
    return version;
  }

  public ModelHttpRequestHead setVersion(String version) {
    this.version = version;
    return this;
  }

  public String getMethod() {
    return method;
  }

  public ModelHttpRequestHead setMethod(String method) {
    this.method = method;
    return this;
  }

  public ModelHttpUrl getUrl() {
    return url;
  }

  public ModelHttpRequestHead setUrl(ModelHttpUrl url) {
    this.url = url;
    return this;
  }

  public ModelHttpHeaders getHeaders() {
    return headers;
  }

  public ModelHttpRequestHead setHeaders(ModelHttpHeaders headers) {
    this.headers = headers;
    return this;
  }

  public ModelHttpRequestHeadBuilder toBuilder() {
    return new ModelHttpRequestHeadBuilder(this);
  }

  public ModelHttpRequest toRequest() throws IOException {
    return new ModelHttpRequest(this);
  }

  public ModelHttpRequest toRequest(ModelHttpEntity entity) throws IOException {
    return new ModelHttpRequest(this, entity);
  }

  public ModelHttpRequest toRequest(InputStream entity) throws IOException {
    return new ModelHttpRequest(this, entity);
  }

  @Override
  public Optional<Object> getProperty(String name) {
    return Optional.ofNullable(properties.get(name));
  }

  @Override
  public void setProperty(String name, Object value) {
    if (value != null) {
      properties.put(name, value);
    } else {
      properties.remove(name);
    }
  }

  @Override
  public Set<String> getPropertyNames() {
    return unmodifiableSet(properties.keySet());
  }

  @Override
  public String toString() {
    return "ModelHttpRequestHead [version=" + version + ", method=" + method + ", url=" + url
        + ", headers=" + headers + "]";
  }
}
