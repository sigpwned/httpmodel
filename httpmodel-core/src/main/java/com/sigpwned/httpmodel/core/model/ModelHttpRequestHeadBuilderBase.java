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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import com.sigpwned.httpmodel.core.util.ModelHttpVersions;

/**
 * Models an HTTP request
 */
public abstract class ModelHttpRequestHeadBuilderBase<QueryStringBuilderT extends ModelHttpQueryStringBuilderBase<QueryStringBuilderT>, UrlBuilderT extends ModelHttpUrlBuilderBase<QueryStringBuilderT, UrlBuilderT>, HeadersBuilderT extends ModelHttpHeadersBuilderBase<HeadersBuilderT>, BuilderT extends ModelHttpRequestHeadBuilderBase<QueryStringBuilderT, UrlBuilderT, HeadersBuilderT, BuilderT>> {
  private String version;

  private String method;

  private UrlBuilderT url;

  private HeadersBuilderT headers;

  private Map<String, Object> properties;

  public ModelHttpRequestHeadBuilderBase() {
    this.version = ModelHttpVersions.DEFAULT;
    this.headers = newHeadersBuilder();
    this.properties = new HashMap<>();
  }

  public ModelHttpRequestHeadBuilderBase(BuilderT that) {
    this.version = Optional.ofNullable(that.version()).orElse(ModelHttpVersions.DEFAULT);
    this.method = that.method();
    this.url = that.url();
    this.headers = that.headers();
    this.properties = that.properties();
  }

  public ModelHttpRequestHeadBuilderBase(ModelHttpRequestHead that) {
    this.version = Optional.ofNullable(that.getVersion()).orElse(ModelHttpVersions.DEFAULT);
    this.method = that.getMethod();
    this.url = that.getUrl() != null ? newUrlBuilder().assign(that.getUrl()) : null;
    this.headers = newHeadersBuilder().assign(that.getHeaders());
    this.properties = new HashMap<>(that.getProperties());
  }


  public String version() {
    return version;
  }

  @SuppressWarnings("unchecked")
  public BuilderT version(String version) {
    this.version = version;
    return (BuilderT) this;
  }

  public String method() {
    return method;
  }

  @SuppressWarnings("unchecked")
  public BuilderT method(String method) {
    this.method = method;
    return (BuilderT) this;
  }

  public UrlBuilderT url() {
    return url;
  }

  @SuppressWarnings("unchecked")
  public BuilderT url(UrlBuilderT url) {
    this.url = url;
    return (BuilderT) this;
  }

  @SuppressWarnings("unchecked")
  public BuilderT url(ModelHttpUrl newUrl) {
    if (url == null)
      url = newUrlBuilder();
    url.assign(newUrl);
    return (BuilderT) this;
  }

  public HeadersBuilderT headers() {
    return headers;
  }

  @SuppressWarnings("unchecked")
  public BuilderT headers(HeadersBuilderT headers) {
    this.headers = headers;
    return (BuilderT) this;
  }

  @SuppressWarnings("unchecked")
  public BuilderT headers(ModelHttpHeaders newHeaders) {
    if (headers == null)
      headers = newHeadersBuilder();
    if (newHeaders != null) {
      headers.assign(newHeaders);
    } else {
      headers.clear();
    }
    return (BuilderT) this;
  }

  @SuppressWarnings("unchecked")
  public BuilderT property(String name, Object value) {
    if (value != null) {
      properties.put(name, value);
    } else {
      properties.remove(name);
    }
    return (BuilderT) this;
  }

  @SuppressWarnings("unchecked")
  public BuilderT properties(Map<String, Object> properties) {
    for (Map.Entry<String, Object> property : properties.entrySet())
      property(property.getKey(), property.getValue());
    return (BuilderT) this;
  }

  protected Map<String, Object> properties() {
    // TODO I'd really like this to be unmodifiable...
    return properties;
  }

  protected ModelHttpRequestHead build() {
    return new ModelHttpRequestHead(version(), method(),
        Optional.ofNullable(url()).map(UrlBuilderT::build)
            .orElseThrow(() -> new IllegalStateException("no url")),
        Optional.ofNullable(headers()).map(HeadersBuilderT::build)
            .orElseThrow(() -> new IllegalStateException("no headers")),
        properties());
  }

  protected abstract UrlBuilderT newUrlBuilder();

  protected abstract HeadersBuilderT newHeadersBuilder();
}
