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
package com.sigpwned.httpmodel;

import java.util.Objects;
import java.util.Optional;
import com.sigpwned.httpmodel.util.ModelHttpMethods;
import com.sigpwned.httpmodel.util.ModelHttpVersions;

/**
 * Models an HTTP request
 */
public class ModelHttpRequest {
  public static ModelHttpRequest of(String version, String method, ModelHttpUrl url,
      ModelHttpEntity entity) {
    return of(version, method, url, ModelHttpHeaders.of(), entity);
  }

  public static ModelHttpRequest of(String version, String method, ModelHttpUrl url,
      ModelHttpHeaders headers, ModelHttpEntity entity) {
    return new ModelHttpRequest(version, method, url, headers, entity);
  }

  public static ModelHttpRequest of(String version, String method, ModelHttpUrl url,
      Optional<ModelHttpEntity> entity) {
    return of(version, method, url, ModelHttpHeaders.of(), entity.orElse(null));
  }

  public static ModelHttpRequest of(String version, String method, ModelHttpUrl url,
      ModelHttpHeaders headers, Optional<ModelHttpEntity> entity) {
    return new ModelHttpRequest(version, method, url, headers, entity.orElse(null));
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

  private final ModelHttpEntity entity;

  public ModelHttpRequest(String version, String method, ModelHttpUrl url, ModelHttpHeaders headers,
      ModelHttpEntity entity) {
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
    this.entity = entity;
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

  /**
   * @return the entity
   */
  public Optional<ModelHttpEntity> getEntity() {
    return Optional.ofNullable(entity);
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
    ModelHttpRequest other = (ModelHttpRequest) obj;
    return Objects.equals(entity, other.entity) && Objects.equals(headers, other.headers)
        && Objects.equals(method, other.method) && Objects.equals(url, other.url)
        && Objects.equals(version, other.version);
  }

  @Override
  public String toString() {
    return "ModelHttpRequest [version=" + version + ", method=" + method + ", url=" + url
        + ", headers=" + headers + ", entity=" + entity + "]";
  }
}
