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
package com.sigpwned.httpmodel.core;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import com.sigpwned.httpmodel.core.util.ModelHttpSchemes;

/**
 * Models a URL
 */
public class ModelHttpUrl {
  /**
   * Parses a valid URL
   * 
   * @throws IllegalArgumentException if the string is not a valid URL
   * 
   * @see #toString()
   */
  public static ModelHttpUrl fromString(String s) {
    try {
      return fromUrl(new URL(s));
    } catch (MalformedURLException e) {
      throw new IllegalArgumentException("must be a valid URL");
    }
  }

  /**
   * Converts a URL into an instance of ModelHttpUrl
   * 
   * @throws IllegalArgumentException if the query string is not valid
   *
   * @see #toUrl()
   */
  public static ModelHttpUrl fromUrl(URL url) {
    String scheme = url.getProtocol();
    ModelHttpHost host = ModelHttpHost.fromString(url.getHost());
    Integer port = url.getPort() == -1 ? null : url.getPort();
    String path = url.getPath().isEmpty() ? "/" : url.getPath();
    ModelHttpQueryString queryString =
        Optional.ofNullable(url.getQuery()).map(ModelHttpQueryString::fromString).orElse(null);
    return of(scheme, ModelHttpAuthority.of(host, port), path, queryString);
  }

  public static ModelHttpUrl of(String scheme, ModelHttpAuthority authority, String path,
      ModelHttpQueryString queryString) {
    return new ModelHttpUrl(scheme, authority, path, queryString);
  }

  /**
   * @see ModelHttpSchemes
   */
  private final String scheme;

  private final ModelHttpAuthority authority;

  private final String path;

  private final ModelHttpQueryString queryString;

  public ModelHttpUrl(String scheme, ModelHttpAuthority authority, String path,
      ModelHttpQueryString queryString) {
    if (scheme == null)
      throw new NullPointerException();
    if (authority == null)
      throw new NullPointerException();
    if (path == null)
      throw new NullPointerException();
    if (!path.startsWith("/"))
      throw new IllegalArgumentException("path must be absolute");
    this.scheme = scheme;
    this.authority = authority;
    this.path = path;
    this.queryString = queryString;
  }

  /**
   * @return the scheme
   */
  public String getScheme() {
    return scheme;
  }

  /**
   * @return the authority
   */
  public ModelHttpAuthority getAuthority() {
    return authority;
  }

  /**
   * @return the path
   */
  public String getPath() {
    return path;
  }

  /**
   * @return the parameters
   */
  public Optional<ModelHttpQueryString> getQueryString() {
    return Optional.ofNullable(queryString);
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
    ModelHttpUrl other = (ModelHttpUrl) obj;
    return Objects.equals(authority, other.authority) && Objects.equals(path, other.path)
        && Objects.equals(queryString, other.queryString) && Objects.equals(scheme, other.scheme);
  }

  /**
   * Converts this object into a valid URL
   * 
   * @throws IllegalArgumentException if the URL is not valid, which may happen if scheme or path
   *         are not valid
   */
  public URL toUrl() {
    try {
      return new URL(toString());
    } catch (MalformedURLException e) {
      throw new IllegalArgumentException("not a valid URL");
    }
  }

  /**
   * Converts this object into a valid URL string
   * 
   * @see #fromString(String)
   */
  @Override
  public String toString() {
    String result = getScheme() + "://" + getAuthority() + getPath();
    if (getQueryString().isPresent()) {
      result = result + "?" + getQueryString().get().toString();
    }
    return result;
  }
}
