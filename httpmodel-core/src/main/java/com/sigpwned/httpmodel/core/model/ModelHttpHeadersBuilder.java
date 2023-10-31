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

import static java.util.Collections.singletonList;
import static java.util.Collections.unmodifiableList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import com.sigpwned.httpmodel.core.model.ModelHttpHeaders.Header;

public class ModelHttpHeadersBuilder {
  private final List<Header> headers;

  public ModelHttpHeadersBuilder() {
    this.headers = new ArrayList<>();
  }

  public ModelHttpHeadersBuilder(ModelHttpHeadersBuilder that) {
    if (that == null)
      throw new NullPointerException();
    this.headers = new ArrayList<>(that.headers());
  }

  public ModelHttpHeadersBuilder(ModelHttpHeaders that) {
    if (that == null)
      throw new NullPointerException();
    this.headers = new ArrayList<>(that.getHeaders());
  }

  public ModelHttpHeadersBuilder addHeaderFirst(String name, String value) {
    return addHeaderFirst(new ModelHttpHeaders.Header(name, value));
  }

  public ModelHttpHeadersBuilder addHeaderFirst(ModelHttpHeaders.Header header) {
    if (header == null)
      throw new NullPointerException();
    headers.add(0, header);
    return this;
  }

  public ModelHttpHeadersBuilder addHeaderLast(String name, String value) {
    return addHeaderLast(new ModelHttpHeaders.Header(name, value));
  }

  public ModelHttpHeadersBuilder addHeaderLast(ModelHttpHeaders.Header header) {
    if (header == null)
      throw new NullPointerException();
    headers.add(headers.size(), header);
    return this;
  }

  public ModelHttpHeadersBuilder removeFirstHeader(String name) {
    removeHeaderMatching(h -> h.getName().equalsIgnoreCase(name), true);
    return this;
  }

  public ModelHttpHeadersBuilder removeAllHeaders(String name) {
    removeHeaderMatching(h -> h.getName().equalsIgnoreCase(name), false);
    return this;
  }

  public ModelHttpHeadersBuilder setFirstHeader(String name, String value) {
    removeFirstHeader(name);
    if (value != null)
      addHeaderFirst(name, value);
    return this;
  }

  public ModelHttpHeadersBuilder setOnlyHeader(String name, String value) {
    if (value != null)
      return setAllHeaders(name, singletonList(value));
    else
      return removeAllHeaders(name);
  }

  public ModelHttpHeadersBuilder setAllHeaders(String name, List<String> values) {
    removeAllHeaders(name);
    if (values != null) {
      for (String value : values)
        addHeaderLast(name, value);
    }
    return this;
  }

  public void clear() {
    headers.clear();
  }

  public boolean isEmpty() {
    return headers.isEmpty();
  }

  private void removeHeaderMatching(Predicate<ModelHttpHeaders.Header> test, boolean firstOnly) {
    Iterator<ModelHttpHeaders.Header> iterator = headers.iterator();
    while (iterator.hasNext()) {
      if (test.test(iterator.next())) {
        iterator.remove();
        if (firstOnly)
          break;
      }
    }
  }

  public List<Header> headers() {
    return unmodifiableList(headers);
  }

  public ModelHttpHeaders build() {
    return new ModelHttpHeaders(new ArrayList<>(headers));
  }

  public ModelHttpHeaders buildOrNull() {
    return isEmpty() ? null : build();
  }

  @Override
  public int hashCode() {
    return Objects.hash(headers);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ModelHttpHeadersBuilder other = (ModelHttpHeadersBuilder) obj;
    return Objects.equals(headers, other.headers);
  }

  @Override
  public String toString() {
    return "ModelHttpHeadersBuilder [headers=" + headers + "]";
  }
}
