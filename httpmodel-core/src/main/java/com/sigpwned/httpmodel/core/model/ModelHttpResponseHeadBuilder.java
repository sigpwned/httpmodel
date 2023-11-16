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

/**
 * Models an HTTP response
 */
public class ModelHttpResponseHeadBuilder extends
    ModelHttpResponseHeadBuilderBase<ModelHttpResponseHeadHeadersBuilder, ModelHttpResponseHeadBuilder> {
  public ModelHttpResponseHeadBuilder() {}

  public ModelHttpResponseHeadBuilder(ModelHttpResponseHeadBuilder that) {
    super(that);
  }

  public ModelHttpResponseHeadBuilder(ModelHttpResponseHead that) {
    super(that);
  }

  @Override
  protected ModelHttpResponseHeadHeadersBuilder newHeadersBuilder() {
    return new ModelHttpResponseHeadHeadersBuilder(this);
  }
}
