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
import com.sigpwned.httpmodel.core.model.ModelHttpQueryString.Parameter;

public abstract class ModelHttpQueryStringBuilderBase<BuilderT extends ModelHttpQueryStringBuilderBase<BuilderT>> {
  private final ModelHttpQueryString building;

  public ModelHttpQueryStringBuilderBase() {
    this(emptyList());
  }

  public ModelHttpQueryStringBuilderBase(BuilderT that) {
    this(that.parameters());
  }

  public ModelHttpQueryStringBuilderBase(ModelHttpQueryString that) {
    this(that.getParameters());
  }

  public ModelHttpQueryStringBuilderBase(List<Parameter> parameters) {
    this.building = new ModelHttpQueryString(parameters);
  }

  @SuppressWarnings("unchecked")
  public BuilderT addParameterFirst(String name, String value) {
    building.addParameterFirst(name, value);
    return (BuilderT) this;
  }

  @SuppressWarnings("unchecked")
  public BuilderT addParameterFirst(ModelHttpQueryString.Parameter parameter) {
    building.addParameterFirst(parameter);
    return (BuilderT) this;
  }

  @SuppressWarnings("unchecked")
  public BuilderT addParameterLast(String name, String value) {
    building.addParameterLast(new ModelHttpQueryString.Parameter(name, value));
    return (BuilderT) this;
  }

  @SuppressWarnings("unchecked")
  public BuilderT addParameterLast(ModelHttpQueryString.Parameter parameter) {
    building.addParameterLast(parameter);
    return (BuilderT) this;
  }

  @SuppressWarnings("unchecked")
  public BuilderT removeFirstParameter(String name) {
    building.removeFirstParameter(name);
    return (BuilderT) this;
  }

  @SuppressWarnings("unchecked")
  public BuilderT removeAllParameters(String name) {
    building.removeAllParameters(name);
    return (BuilderT) this;
  }

  @SuppressWarnings("unchecked")
  public BuilderT setFirstParameter(String name, String value) {
    building.setFirstParameter(name, value);
    return (BuilderT) this;
  }

  @SuppressWarnings("unchecked")
  public BuilderT setOnlyParameter(String name, String value) {
    building.setOnlyParameter(name, value);
    return (BuilderT) this;
  }

  @SuppressWarnings("unchecked")
  public BuilderT setAllParameters(String name, List<String> values) {
    building.setAllParameters(name, values);
    return (BuilderT) this;
  }

  @SuppressWarnings("unchecked")
  public BuilderT assign(ModelHttpQueryString value) {
    building.clear();
    for (Parameter parameter : value)
      building.addParameterLast(parameter);
    return (BuilderT) this;
  }

  public void clear() {
    building.clear();
  }

  public boolean isEmpty() {
    return building.isEmpty();
  }

  public List<ModelHttpQueryString.Parameter> parameters() {
    return unmodifiableList(building.getParameters());
  }

  protected ModelHttpQueryString build() {
    return building;
  }
}
