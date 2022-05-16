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

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import com.sigpwned.httpmodel.util.ModelHttpEncodings;

public class ModelHttpQueryString {
  private static final Pattern AMPERSAND = Pattern.compile("&");

  private static final Pattern EQUALS = Pattern.compile("=");

  public static class Parameter {
    public static Parameter fromString(String nv) {
      String[] parts = EQUALS.split(nv, 2);
      String n = ModelHttpEncodings.urldecode(parts[0]);
      String v = parts.length == 2 ? ModelHttpEncodings.urldecode(parts[1]) : "";
      return Parameter.of(n, v);
    }

    public static Parameter of(String name, String value) {
      return new Parameter(name, value);
    }

    private final String name;
    private final String value;

    public Parameter(String name, String value) {
      if (name == null)
        throw new NullPointerException();
      if (value == null)
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
      Parameter other = (Parameter) obj;
      return Objects.equals(name, other.name) && Objects.equals(value, other.value);
    }

    @Override
    public String toString() {
      String result = ModelHttpEncodings.urlencode(getName());
      if (getValue() != null)
        result = result + "=" + ModelHttpEncodings.urlencode(getValue());
      return result;
    }
  }

  public static ModelHttpQueryString fromString(String s) {
    return ModelHttpQueryString.of(AMPERSAND.splitAsStream(s).filter(nv -> !nv.isEmpty())
        .map(Parameter::fromString).collect(toList()));
  }

  public static ModelHttpQueryString of(List<Parameter> entries) {
    return new ModelHttpQueryString(entries);
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

  @Override
  public String toString() {
    return getParameters().stream().map(Parameter::toString).collect(joining("&"));
  }
}
