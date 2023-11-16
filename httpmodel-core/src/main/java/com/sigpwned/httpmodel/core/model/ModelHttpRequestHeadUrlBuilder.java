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

public class ModelHttpRequestHeadUrlBuilder extends
    ModelHttpUrlChildBuilderBase<ModelHttpRequestHeadBuilder, ModelHttpRequestHeadUrlQueryStringBuilder, ModelHttpRequestHeadUrlBuilder> {
  public ModelHttpRequestHeadUrlBuilder(ModelHttpRequestHeadBuilder parent) {
    super(parent);
  }

  public ModelHttpRequestHeadUrlBuilder(ModelHttpRequestHeadBuilder parent,
      ModelHttpRequestHeadUrlBuilder that) {
    super(parent, that.scheme(), that.authority(), that.path(), that.queryString());
  }

  @Override
  protected ModelHttpRequestHeadUrlQueryStringBuilder newQueryStringBuilder() {
    return new ModelHttpRequestHeadUrlQueryStringBuilder(this);
  }
}
