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

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import java.util.List;
import com.sigpwned.httpmodel.core.model.ModelHttpHeaders.Header;

public abstract class ModelHttpHeadersBuilderBase<BuilderT extends ModelHttpHeadersBuilderBase<BuilderT>> {
  private final ModelHttpHeaders building;

  public ModelHttpHeadersBuilderBase() {
    this(emptyList());
  }

  public ModelHttpHeadersBuilderBase(BuilderT that) {
    this(that.headers());
  }

  public ModelHttpHeadersBuilderBase(ModelHttpHeaders that) {
    this(that.getHeaders());
  }

  public ModelHttpHeadersBuilderBase(List<Header> headers) {
    this.building = new ModelHttpHeaders(headers);
  }

  @SuppressWarnings("unchecked")
  public BuilderT addHeaderFirst(String name, String value) {
    building.addHeaderFirst(name, value);
    return (BuilderT) this;
  }

  @SuppressWarnings("unchecked")
  public BuilderT addHeaderFirst(ModelHttpHeaders.Header header) {
    building.addHeaderFirst(header);
    return (BuilderT) this;
  }

  @SuppressWarnings("unchecked")
  public BuilderT addHeaderLast(String name, String value) {
    building.addHeaderLast(name, value);
    return (BuilderT) this;
  }

  @SuppressWarnings("unchecked")
  public BuilderT addHeaderLast(ModelHttpHeaders.Header header) {
    building.addHeaderLast(header);
    return (BuilderT) this;
  }

  @SuppressWarnings("unchecked")
  public BuilderT removeFirstHeader(String name) {
    building.removeFirstHeader(name);
    return (BuilderT) this;
  }

  @SuppressWarnings("unchecked")
  public BuilderT removeAllHeaders(String name) {
    building.removeAllHeaders(name);
    return (BuilderT) this;
  }

  @SuppressWarnings("unchecked")
  public BuilderT setFirstHeader(String name, String value) {
    building.setFirstHeader(name, value);
    return (BuilderT) this;
  }

  @SuppressWarnings("unchecked")
  public BuilderT setOnlyHeader(String name, String value) {
    building.setOnlyHeader(name, value);
    return (BuilderT) this;
  }

  @SuppressWarnings("unchecked")
  public BuilderT setAllHeaders(String name, List<String> values) {
    building.setAllHeaders(name, values);
    return (BuilderT) this;
  }

  @SuppressWarnings("unchecked")
  public BuilderT clear() {
    building.clear();
    return (BuilderT) this;
  }

  public boolean isEmpty() {
    return building.isEmpty();
  }

  public List<Header> headers() {
    return unmodifiableList(building.getHeaders());
  }

  @SuppressWarnings("unchecked")
  public BuilderT assign(ModelHttpHeaders value) {
    building.clear();
    for (Header header : value)
      building.addHeaderLast(header);
    return (BuilderT) this;
  }

  protected ModelHttpHeaders build() {
    return building;
  }
}
