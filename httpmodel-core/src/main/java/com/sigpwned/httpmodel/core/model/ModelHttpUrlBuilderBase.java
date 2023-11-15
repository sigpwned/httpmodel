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

import java.util.Optional;
import com.sigpwned.httpmodel.core.util.ModelHttpSchemes;

/**
 * Models a URL
 */
public abstract class ModelHttpUrlBuilderBase<QueryStringBuilderT extends ModelHttpQueryStringBuilderBase<QueryStringBuilderT>, BuilderT extends ModelHttpUrlBuilderBase<QueryStringBuilderT, BuilderT>> {
  /**
   * @see ModelHttpSchemes
   */
  private String scheme;

  private ModelHttpAuthority authority;

  private String path;

  private QueryStringBuilderT queryString;

  public ModelHttpUrlBuilderBase() {
    this(null, null, null, null);
  }

  public ModelHttpUrlBuilderBase(BuilderT that) {
    this(that.scheme(), that.authority(), that.path(), that.queryString());
  }

  public ModelHttpUrlBuilderBase(String scheme, ModelHttpAuthority authority, String path,
      QueryStringBuilderT queryString) {
    this.scheme = scheme;
    this.authority = authority;
    this.path = path;
    this.queryString = Optional.ofNullable(queryString).orElseGet(this::newQueryStringBuilder);
  }

  // public ModelHttpUrlBuilder(ModelHttpUrl that) {
  // if (that == null)
  // throw new NullPointerException();
  // this.scheme = that.getScheme();
  // this.authority = that.getAuthority();
  // this.path = that.getPath();
  // this.queryString = that.getQueryString();
  // }

  public String scheme() {
    return scheme;
  }

  @SuppressWarnings("unchecked")
  public BuilderT scheme(String scheme) {
    this.scheme = scheme;
    return (BuilderT) this;
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

  @SuppressWarnings("unchecked")
  public BuilderT authority(ModelHttpAuthority authority) {
    this.authority = authority;
    return (BuilderT) this;
  }

  public String path() {
    return path;
  }

  @SuppressWarnings("unchecked")
  public BuilderT path(String path) {
    this.path = path;
    return (BuilderT) this;
  }

  public QueryStringBuilderT queryString() {
    return queryString;
  }

  protected ModelHttpUrl build() {
    return new ModelHttpUrl(scheme(), authority(), path(), queryString().build());
  }

  protected abstract QueryStringBuilderT newQueryStringBuilder();
}
