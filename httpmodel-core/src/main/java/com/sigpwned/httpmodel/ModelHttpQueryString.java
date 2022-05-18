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
import java.util.regex.Pattern;
import java.util.stream.Stream;
import com.sigpwned.httpmodel.ModelHttpQueryString.Parameter;
import com.sigpwned.httpmodel.util.ModelHttpEncodings;

/**
 * Models an HTTP query string, e.g., alpha=bravo&amp;charlie=delta
 */
public class ModelHttpQueryString implements Iterable<Parameter> {
  private static final Pattern AMPERSAND = Pattern.compile("&");

  private static final Pattern EQUALS = Pattern.compile("=");

  /**
   * Models a single query parameter, e.g., alpha=bravo
   */
  public static class Parameter {

    /**
     * Parses a valid query parameter. The name and value are automatically urldecoded. If the query
     * parameter does not contain "=", then the value is considered absent.
     * 
     * @throws IllegalArgumentException if the query string is not valid
     * 
     * @see ModelHttpEncodings#urldecode(String)
     * @see #toString()
     */
    public static Parameter fromString(String nv) {
      String[] parts = EQUALS.split(nv, 2);
      String n = ModelHttpEncodings.urldecode(parts[0]);
      String v = parts.length == 2 ? ModelHttpEncodings.urldecode(parts[1]) : "";
      return Parameter.of(n, v);
    }

    public static Parameter of(String name, Optional<String> value) {
      return of(name, value.orElse(null));
    }

    public static Parameter of(String name, String value) {
      return new Parameter(name, value);
    }

    private final String name;
    private final String value;

    public Parameter(String name, String value) {
      if (name == null)
        throw new NullPointerException();
      this.name = name;
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
    public Optional<String> getValue() {
      return Optional.ofNullable(value);
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
      Parameter other = (Parameter) obj;
      return Objects.equals(name, other.name) && Objects.equals(value, other.value);
    }

    /**
     * Converts this object into a valid query parameter string. The name and value are
     * automatically urlencoded.
     * 
     * @see ModelHttpEncodings#urlencode(String)
     * @see #fromString(String)
     */
    @Override
    public String toString() {
      String result = ModelHttpEncodings.urlencode(getName());
      if (getValue().isPresent())
        result = result + "=" + ModelHttpEncodings.urlencode(getValue().get());
      return result;
    }
  }

  /**
   * Parses a valid query string.
   * 
   * @throws IllegalArgumentException if the query string is not valid
   *
   * @see Parameter#fromString(String)
   * @see #toString()
   */
  public static ModelHttpQueryString fromString(String s) {
    return ModelHttpQueryString.of(AMPERSAND.splitAsStream(s).filter(nv -> !nv.isEmpty())
        .map(Parameter::fromString).collect(toList()));
  }

  public static ModelHttpQueryString of(Parameter... parameters) {
    return new ModelHttpQueryString(asList(parameters));
  }

  public static ModelHttpQueryString of(List<Parameter> parameters) {
    return new ModelHttpQueryString(parameters);
  }

  private final List<Parameter> parameters;

  public ModelHttpQueryString(List<Parameter> entries) {
    if (entries == null)
      throw new NullPointerException();
    this.parameters = unmodifiableList(entries);
  }

  /**
   * @return the entries
   */
  public List<Parameter> getParameters() {
    return parameters;
  }

  public Optional<Parameter> findFirstParameterByName(String name) {
    return stream().filter(p -> p.getName().equals(name)).findFirst();
  }

  public List<Parameter> findAllParametersByName(String name) {
    return stream().filter(p -> p.getName().equals(name)).collect(toList());
  }

  @Override
  public int hashCode() {
    return Objects.hash(parameters);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ModelHttpQueryString other = (ModelHttpQueryString) obj;
    return Objects.equals(parameters, other.parameters);
  }

  /**
   * Converts this object to a valid query string
   * 
   * @see #fromString(String)
   */
  @Override
  public String toString() {
    return stream().map(Parameter::toString).collect(joining("&"));
  }

  @Override
  public Iterator<Parameter> iterator() {
    return getParameters().iterator();
  }

  public Stream<Parameter> stream() {
    return getParameters().stream();
  }
}
