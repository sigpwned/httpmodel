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
import com.sigpwned.httpmodel.core.util.ModelHttpSchemes;

/**
 * Models a URL
 */
public class ModelHttpUrlBuilder {
  /**
   * @see ModelHttpSchemes
   */
  private String scheme;

  private ModelHttpAuthority authority;

  private String path;

  private ModelHttpQueryString queryString;

  public ModelHttpUrlBuilder() {}

  public ModelHttpUrlBuilder(ModelHttpUrlBuilder that) {
    if (that == null)
      throw new NullPointerException();
    this.scheme = that.scheme;
    this.authority = that.authority;
    this.path = that.path;
    this.queryString = that.queryString;
  }

  public ModelHttpUrlBuilder(ModelHttpUrl that) {
    if (that == null)
      throw new NullPointerException();
    this.scheme = that.getScheme();
    this.authority = that.getAuthority();
    this.path = that.getPath();
    this.queryString = that.getQueryString().orElse(null);
  }

  public String scheme() {
    return scheme;
  }

  public ModelHttpUrlBuilder scheme(String scheme) {
    this.scheme = scheme;
    return this;
  }

  /*
   * private ModelHttpAuthority authority;
   *
   * private String path;
   *
   * private ModelHttpQueryString queryString;
   *
   */

  public ModelHttpAuthority authority() {
    return authority;
  }

  public ModelHttpUrlBuilder authority(ModelHttpAuthority authority) {
    this.authority = authority;
    return this;
  }

  public String path() {
    return path;
  }

  public ModelHttpUrlBuilder path(String path) {
    this.path = path;
    return this;
  }

  public ModelHttpQueryString queryString() {
    return queryString;
  }

  public ModelHttpUrlBuilder queryString(ModelHttpQueryString queryString) {
    this.queryString = queryString;
    return this;
  }

  public ModelHttpUrl build() {
    return new ModelHttpUrl(scheme(), authority(), path(), queryString());
  }

  @Override
  public int hashCode() {
    return Objects.hash(authority, path, queryString, scheme);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ModelHttpUrlBuilder other = (ModelHttpUrlBuilder) obj;
    return Objects.equals(authority, other.authority) && Objects.equals(path, other.path)
        && Objects.equals(queryString, other.queryString) && Objects.equals(scheme, other.scheme);
  }

  @Override
  public String toString() {
    return "ModelHttpUrlBuilder [scheme=" + scheme + ", authority=" + authority + ", path=" + path
        + ", queryString=" + queryString + "]";
  }
}
