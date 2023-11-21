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

  /**
   * Appends {@code suffix} to this URL's current {@code path}. The two strings will be separated by
   * exactly one {@code /}, regardless of whether {@code path} ends with {@code /} or {@code suffix}
   * begins with {@code /}. If {@code suffix} is empty, then this method has no effect.
   *
   * @throws NullPointerException if {@code suffix} is {@code null}
   */
  @SuppressWarnings("unchecked")
  public BuilderT appendPath(String suffix) {
    if (suffix == null)
      throw new NullPointerException();

    while (suffix.startsWith("/"))
      suffix = suffix.substring(1, suffix.length());
    if (suffix.isEmpty())
      return (BuilderT) this;

    String newPath = path();
    while (newPath.endsWith("/"))
      newPath = newPath.substring(0, newPath.length() - 1);
    newPath = newPath + "/" + suffix;

    return path(newPath);
  }

  public QueryStringBuilderT queryString() {
    return queryString;
  }

  @SuppressWarnings("unchecked")
  public BuilderT queryString(QueryStringBuilderT queryString) {
    this.queryString = queryString;
    return (BuilderT) this;
  }

  @SuppressWarnings("unchecked")
  public BuilderT queryString(ModelHttpQueryString newQueryString) {
    if (queryString == null)
      queryString = newQueryStringBuilder();
    if (newQueryString != null) {
      queryString.assign(newQueryString);
    } else {
      queryString.clear();
    }
    return (BuilderT) this;
  }

  @SuppressWarnings("unchecked")
  public BuilderT assign(ModelHttpUrl url) {
    scheme(url.getScheme());
    authority(url.getAuthority());
    path(url.getPath());
    queryString(url.getQueryString());
    return (BuilderT) this;
  }

  protected ModelHttpUrl build() {
    return new ModelHttpUrl(scheme(), authority(), path(),
        Optional.ofNullable(queryString()).map(QueryStringBuilderT::build).orElse(null));
  }

  protected abstract QueryStringBuilderT newQueryStringBuilder();
}
