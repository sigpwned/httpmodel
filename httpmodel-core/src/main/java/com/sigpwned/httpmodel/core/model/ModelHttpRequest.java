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

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import com.sigpwned.httpmodel.core.io.ByteFilterSource;
import com.sigpwned.httpmodel.core.model.ModelHttpHeaders.Header;
import com.sigpwned.httpmodel.core.util.ModelHttpHeaderNames;
import com.sigpwned.httpmodel.core.util.ModelHttpMethods;
import com.sigpwned.httpmodel.core.util.ModelHttpVersions;
import com.sigpwned.httpmodel.core.util.MorePropertiesBearing;

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
  private String version;

  /**
   * @see ModelHttpMethods
   */
  private String method;

  private ModelHttpUrl url;

  private ModelHttpHeaders headers;

  /**
   * This is not part of HTTP. Rather, it is used by applications to store arbitrary state on the
   * request during processing.
   */
  private Map<String, Object> properties;

  public ModelHttpRequest(ModelHttpRequestHead head) throws IOException {
    this(head, (InputStream) null);
  }

  public ModelHttpRequest(ModelHttpRequestHead head, ModelHttpEntity entity) throws IOException {
    this(head.getVersion(), head.getMethod(), head.getUrl(), head.getHeaders(),
        MorePropertiesBearing.toMap(head), entity);
  }

  public ModelHttpRequest(ModelHttpRequestHead head, InputStream entity) throws IOException {
    this(head.getVersion(), head.getMethod(), head.getUrl(), head.getHeaders(),
        MorePropertiesBearing.toMap(head), entity);
  }

  public ModelHttpRequest(String version, String method, ModelHttpUrl url, ModelHttpHeaders headers,
      ModelHttpEntity entity) throws IOException {
    this(version, method, url, headers, emptyMap(), entity);
  }

  public ModelHttpRequest(String version, String method, ModelHttpUrl url, ModelHttpHeaders headers,
      Map<String, Object> properties, ModelHttpEntity entity) throws IOException {
    this(version, method, url,
        entity != null
            ? headers.toBuilder()
                .setOnlyHeader(ModelHttpHeaderNames.CONTENT_TYPE,
                    entity.getContentType().toString())
                .build()
            : null,
        properties, entity != null ? entity.toInputStream() : null);
  }

  public ModelHttpRequest(String version, String method, ModelHttpUrl url, ModelHttpHeaders headers,
      InputStream entity) throws IOException {
    this(version, method, url, headers, emptyMap(), entity);
  }

  public ModelHttpRequest(String version, String method, ModelHttpUrl url, ModelHttpHeaders headers,
      Map<String, Object> properties, InputStream entity) throws IOException {
    super(entity);
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
    this.version = version.toUpperCase();
    this.method = method.toUpperCase();
    this.url = url;
    this.headers = headers;
    this.properties = new HashMap<>(properties);
  }

  public String getVersion() {
    return version;
  }

  public ModelHttpRequest setVersion(String version) {
    if (version == null)
      throw new NullPointerException();
    this.version = version;
    return this;
  }

  public String getMethod() {
    return method;
  }

  public ModelHttpRequest setMethod(String method) {
    if (method == null)
      throw new NullPointerException();
    this.method = method;
    return this;
  }

  public ModelHttpUrl getUrl() {
    return url;
  }

  public ModelHttpRequest setUrl(ModelHttpUrl url) {
    if (url == null)
      throw new NullPointerException();
    this.url = url;
    return this;
  }

  public ModelHttpHeaders getHeaders() {
    return headers;
  }

  public ModelHttpRequest setHeaders(ModelHttpHeaders headers) {
    if (headers == null)
      throw new NullPointerException();
    this.headers = headers;
    return this;
  }

  @Override
  public Optional<ModelHttpMediaType> getContentType() {
    if (!hasEntity())
      return Optional.empty();
    return getHeaders().findFirstHeaderByName(ModelHttpHeaderNames.CONTENT_TYPE)
        .map(Header::getValue).map(ModelHttpMediaType::fromString);
  }

  public void encode(ByteFilterSource filterSource) throws IOException {
    filter(filterSource);
  }

  public Optional<Object> getProperty(String name) {
    return Optional.ofNullable(properties.get(name));
  }

  public void setProperty(String name, Object value) {
    if (value != null) {
      properties.put(name, value);
    } else {
      properties.remove(name);
    }
  }

  public Map<String, Object> getProperties() {
    return unmodifiableMap(properties);
  }

  @Override
  public String toString() {
    return "ModelHttpRequest [version=" + version + ", method=" + method + ", url=" + url
        + ", headers=" + headers + "]";
  }
}
