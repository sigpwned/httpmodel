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

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import com.sigpwned.httpmodel.core.model.ModelHttpHeaders.Header;
import com.sigpwned.httpmodel.core.util.ModelHttpHeaderNames;

/**
 * Models HTTP Headers.
 *
 * @see ModelHttpHeaderNames
 */
public class ModelHttpHeaders implements Iterable<Header> {
  public static ModelHttpHeadersBuilder builder() {
    return new ModelHttpHeadersBuilder();
  }

  /**
   * Models an HTTP header. Header names are lowercased automatically.
   */
  public static class Header {
    public static Header of(String name, String value) {
      return new Header(name, value);
    }

    private final String name;
    private final String value;

    public Header(String name, String value) {
      if (name == null)
        throw new NullPointerException();
      if (value == null)
        throw new NullPointerException();
      this.name = name.toLowerCase();
      this.value = value;
    }

    /**
     * @return the name
     */
    public String getName() {
      return name;
    }

    /**
     * @return the value
     */
    public String getValue() {
      return value;
    }

    @Override
    public int hashCode() {
      return Objects.hash(name, value);
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      Header other = (Header) obj;
      return Objects.equals(name, other.name) && Objects.equals(value, other.value);
    }

    @Override
    public String toString() {
      return getName() + ": " + getValue();
    }
  }

  private final List<ModelHttpHeaders.Header> headers;

  public ModelHttpHeaders() {
    this(emptyList());
  }

  public ModelHttpHeaders(ModelHttpHeaders that) {
    this(that.getHeaders());
  }

  public ModelHttpHeaders(List<Header> headers) {
    if (headers == null)
      throw new NullPointerException();
    this.headers = new ArrayList<>(headers);
  }

  /* default */ ModelHttpHeaders(ModelHttpHeadersBuilder that) {
    this(that.headers());
  }

  /**
   * @return the headers
   */
  public List<Header> getHeaders() {
    return unmodifiableList(headers);
  }

  public Optional<Header> findFirstHeaderByName(String name) {
    return stream().filter(h -> h.getName().equalsIgnoreCase(name)).findFirst();
  }

  public List<Header> findAllHeadersByName(String name) {
    return stream().filter(h -> h.getName().equalsIgnoreCase(name)).collect(toList());
  }

  public ModelHttpHeaders addHeaderFirst(String name, String value) {
    return addHeaderFirst(new ModelHttpHeaders.Header(name, value));
  }

  public ModelHttpHeaders addHeaderFirst(ModelHttpHeaders.Header header) {
    if (header == null)
      throw new NullPointerException();
    headers.add(0, header);
    return this;
  }

  public ModelHttpHeaders addHeaderLast(String name, String value) {
    return addHeaderLast(new ModelHttpHeaders.Header(name, value));
  }

  public ModelHttpHeaders addHeaderLast(ModelHttpHeaders.Header header) {
    if (header == null)
      throw new NullPointerException();
    headers.add(headers.size(), header);
    return this;
  }

  public ModelHttpHeaders removeFirstHeader(String name) {
    removeHeaderMatching(h -> h.getName().equalsIgnoreCase(name), true);
    return this;
  }

  public ModelHttpHeaders removeAllHeaders(String name) {
    removeHeaderMatching(h -> h.getName().equalsIgnoreCase(name), false);
    return this;
  }

  public ModelHttpHeaders setFirstHeader(String name, String value) {
    removeFirstHeader(name);
    if (value != null)
      addHeaderFirst(name, value);
    return this;
  }

  public ModelHttpHeaders setOnlyHeader(String name, String value) {
    if (value != null)
      return setAllHeaders(name, singletonList(value));
    else
      return removeAllHeaders(name);
  }

  public ModelHttpHeaders setAllHeaders(String name, List<String> values) {
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

  public ModelHttpHeadersBuilder toBuilder() {
    return new ModelHttpHeadersBuilder(this);
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
    ModelHttpHeaders other = (ModelHttpHeaders) obj;
    return Objects.equals(headers, other.headers);
  }

  @Override
  public String toString() {
    return stream().map(Objects::toString).collect(joining("\n"));
  }

  @Override
  public Iterator<Header> iterator() {
    return getHeaders().iterator();
  }

  public Stream<Header> stream() {
    return headers.stream();
  }
}
