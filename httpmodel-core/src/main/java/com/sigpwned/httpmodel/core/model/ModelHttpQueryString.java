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

import static java.util.Arrays.asList;
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
import java.util.regex.Pattern;
import java.util.stream.Stream;
import com.sigpwned.httpmodel.core.model.ModelHttpQueryString.Parameter;
import com.sigpwned.httpmodel.core.util.ModelHttpEncodings;
import com.sigpwned.httpmodel.core.util.MoreStreams;

/**
 * Models an HTTP query string, e.g., alpha=bravo&amp;charlie=delta
 */
public class ModelHttpQueryString implements Iterable<Parameter> {
  public static ModelHttpQueryStringBuilder builder() {
    return new ModelHttpQueryStringBuilder();
  }

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

  public ModelHttpQueryString() {
    this(emptyList());
  }

  public ModelHttpQueryString(List<Parameter> parameters) {
    if (parameters == null)
      throw new NullPointerException();
    this.parameters = new ArrayList<>(parameters);
  }

  /* default */ ModelHttpQueryString(ModelHttpQueryStringBuilder that) {
    this(that.parameters());
  }

  /**
   * @return the entries
   */
  public List<Parameter> getParameters() {
    return unmodifiableList(parameters);
  }

  public Optional<Parameter> findFirstParameterByName(String name) {
    return stream().filter(p -> p.getName().equals(name)).findFirst();
  }

  public Optional<String> findFirstParameterValueByName(String name) {
    return findFirstParameterByName(name).flatMap(Parameter::getValue);
  }

  public List<Parameter> findAllParametersByName(String name) {
    return findAllParametersByNameAsStream(name).collect(toList());
  }

  public List<String> findAllParameterValuesByName(String name) {
    return findAllParametersByNameAsStream(name).flatMap(p -> MoreStreams.stream(p.getValue()))
        .collect(toList());
  }

  private Stream<Parameter> findAllParametersByNameAsStream(String name) {
    return stream().filter(p -> p.getName().equals(name));
  }

  public ModelHttpQueryString addParameterFirst(String name, String value) {
    return addParameterFirst(new ModelHttpQueryString.Parameter(name, value));
  }

  public ModelHttpQueryString addParameterFirst(ModelHttpQueryString.Parameter Parameter) {
    if (Parameter == null)
      throw new NullPointerException();
    parameters.add(0, Parameter);
    return this;
  }

  public ModelHttpQueryString addParameterLast(String name, String value) {
    return addParameterLast(new ModelHttpQueryString.Parameter(name, value));
  }

  public ModelHttpQueryString addParameterLast(ModelHttpQueryString.Parameter Parameter) {
    if (Parameter == null)
      throw new NullPointerException();
    parameters.add(parameters.size(), Parameter);
    return this;
  }

  public ModelHttpQueryString removeFirstParameter(String name) {
    removeParameterMatching(h -> h.getName().equalsIgnoreCase(name), true);
    return this;
  }

  public ModelHttpQueryString removeAllParameters(String name) {
    removeParameterMatching(h -> h.getName().equalsIgnoreCase(name), false);
    return this;
  }

  public ModelHttpQueryString setFirstParameter(String name, String value) {
    removeFirstParameter(name);
    if (value != null)
      addParameterFirst(name, value);
    return this;
  }

  public ModelHttpQueryString setOnlyParameter(String name, String value) {
    if (value != null)
      return setAllParameters(name, singletonList(value));
    else
      return removeAllParameters(name);
  }

  public ModelHttpQueryString setAllParameters(String name, List<String> values) {
    removeAllParameters(name);
    if (values != null) {
      for (String value : values)
        addParameterLast(name, value);
    }
    return this;
  }

  public void clear() {
    parameters.clear();
  }

  public boolean isEmpty() {
    return parameters.isEmpty();
  }

  private void removeParameterMatching(Predicate<ModelHttpQueryString.Parameter> test,
      boolean firstOnly) {
    Iterator<ModelHttpQueryString.Parameter> iterator = parameters.iterator();
    while (iterator.hasNext()) {
      if (test.test(iterator.next())) {
        iterator.remove();
        if (firstOnly)
          break;
      }
    }
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

  public ModelHttpQueryStringBuilder toBuilder() {
    return new ModelHttpQueryStringBuilder(this);
  }

  @Override
  public Iterator<Parameter> iterator() {
    return getParameters().iterator();
  }

  public Stream<Parameter> stream() {
    return getParameters().stream();
  }
}
