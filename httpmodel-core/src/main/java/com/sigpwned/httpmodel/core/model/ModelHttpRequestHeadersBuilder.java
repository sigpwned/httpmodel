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
import java.util.List;
import com.sigpwned.httpmodel.core.model.ModelHttpHeaders.Header;

public class ModelHttpRequestHeadersBuilder extends
    ModelHttpHeadersChildBuilderBase<ModelHttpRequestBuilder, ModelHttpRequestHeadersBuilder> {
  /* default */ ModelHttpRequestHeadersBuilder(ModelHttpRequestBuilder parent) {
    this(parent, emptyList());
  }

  /* default */ ModelHttpRequestHeadersBuilder(ModelHttpRequestBuilder parent,
      ModelHttpRequestHeadersBuilder that) {
    this(parent, that.headers());
  }

  /* default */ ModelHttpRequestHeadersBuilder(ModelHttpRequestBuilder parent,
      ModelHttpHeaders that) {
    this(parent, that.getHeaders());
  }

  /* default */ ModelHttpRequestHeadersBuilder(ModelHttpRequestBuilder parent,
      List<Header> headers) {
    super(parent, headers);
  }
}
