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

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import com.sigpwned.httpmodel.ModelHttpHeaders.Header;
import com.sigpwned.httpmodel.util.ModelHttpHeaderNames;

/**
 * Models HTTP Headers.
 * 
 * @see ModelHttpHeaderNames
 */
public class ModelHttpHeaders implements Iterable<Header> {
  /**
   * Models an HTTP header. Header names are lowercased automatically. Assumes all values are valid
   * ISO-8859-1 characters.
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

  public static ModelHttpHeaders of(Header... headers) {
    return new ModelHttpHeaders(asList(headers));
  }

  public static ModelHttpHeaders of(List<Header> headers) {
    return new ModelHttpHeaders(headers);
  }

  private final List<Header> headers;

  public ModelHttpHeaders(List<Header> headers) {
    this.headers = unmodifiableList(headers);
  }

  /**
   * @return the headers
   */
  public List<Header> getHeaders() {
    return headers;
  }

  public Optional<Header> findFirstHeaderByName(String name) {
    return stream().filter(h -> h.getName().equalsIgnoreCase(name)).findFirst();
  }

  public List<Header> findAllHeadersByName(String name) {
    return stream().filter(h -> h.getName().equalsIgnoreCase(name)).collect(toList());
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
    return getHeaders().stream();
  }
}
