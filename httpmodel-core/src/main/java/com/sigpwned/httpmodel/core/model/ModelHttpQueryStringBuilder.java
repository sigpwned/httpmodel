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

public class ModelHttpQueryStringBuilder {
  private final List<ModelHttpQueryString.Parameter> parameters;

  public ModelHttpQueryStringBuilder() {
    this.parameters = new ArrayList<>();
  }

  public ModelHttpQueryStringBuilder(ModelHttpQueryStringBuilder that) {
    if (that == null)
      throw new NullPointerException();
    this.parameters = new ArrayList<>(that.parameters());
  }

  public ModelHttpQueryStringBuilder(ModelHttpQueryString that) {
    if (that == null)
      throw new NullPointerException();
    this.parameters = new ArrayList<>(that.getParameters());
  }

  public ModelHttpQueryStringBuilder addParameterFirst(String name, String value) {
    return addParameterFirst(new ModelHttpQueryString.Parameter(name, value));
  }

  public ModelHttpQueryStringBuilder addParameterFirst(ModelHttpQueryString.Parameter Parameter) {
    if (Parameter == null)
      throw new NullPointerException();
    parameters.add(0, Parameter);
    return this;
  }

  public ModelHttpQueryStringBuilder addParameterLast(String name, String value) {
    return addParameterLast(new ModelHttpQueryString.Parameter(name, value));
  }

  public ModelHttpQueryStringBuilder addParameterLast(ModelHttpQueryString.Parameter Parameter) {
    if (Parameter == null)
      throw new NullPointerException();
    parameters.add(parameters.size(), Parameter);
    return this;
  }

  public ModelHttpQueryStringBuilder removeFirstParameter(String name) {
    removeParameterMatching(h -> h.getName().equalsIgnoreCase(name), true);
    return this;
  }

  public ModelHttpQueryStringBuilder removeAllParameters(String name) {
    removeParameterMatching(h -> h.getName().equalsIgnoreCase(name), false);
    return this;
  }

  public ModelHttpQueryStringBuilder setFirstParameter(String name, String value) {
    removeFirstParameter(name);
    if (value != null)
      addParameterFirst(name, value);
    return this;
  }

  public ModelHttpQueryStringBuilder setAllParameters(String name, String value) {
    return setAllParameters(name, singletonList(value));
  }

  public ModelHttpQueryStringBuilder setAllParameters(String name, List<String> values) {
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

  public List<ModelHttpQueryString.Parameter> parameters() {
    return unmodifiableList(parameters);
  }

  public ModelHttpQueryString build() {
    return new ModelHttpQueryString(new ArrayList<>(parameters));
  }

  public ModelHttpQueryString buildOrNull() {
    return isEmpty() ? null : build();
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
    ModelHttpQueryStringBuilder other = (ModelHttpQueryStringBuilder) obj;
    return Objects.equals(parameters, other.parameters);
  }

  @Override
  public String toString() {
    return "ModelHttpQueryStringBuilder [Parameters=" + parameters + "]";
  }
}
