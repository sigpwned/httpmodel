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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;

public class ModelHttpUrl {
  public static ModelHttpUrl of(String path, ModelHttpQueryString queryString) {
    return new ModelHttpUrl(path, queryString);
  }

  private final String path;

  private final ModelHttpQueryString queryString;

  public ModelHttpUrl(String path, ModelHttpQueryString queryString) {
    if (path == null)
      throw new NullPointerException();
    this.path = path;
    this.queryString = queryString;
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
    return Objects.hash(path, queryString);
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
    return Objects.equals(path, other.path) && Objects.equals(queryString, other.queryString);
  }

  /**
   * @param baseUrl Base URL for the absolute URL. Should not end with "/".
   * @throws MalformedURLException If the final URL is not valid
   */
  public URL toUrl(String baseUrl) throws MalformedURLException {
    return new URL(baseUrl + toString());
  }

  @Override
  public String toString() {
    String result = getPath();
    if (getQueryString().isPresent()) {
      result = result + "?" + getQueryString().get().toString();
    }
    return result;
  }
}
