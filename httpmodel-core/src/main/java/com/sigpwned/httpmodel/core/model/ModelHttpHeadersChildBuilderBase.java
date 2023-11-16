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

public class ModelHttpHeadersChildBuilderBase<ParentT, BuilderT extends ModelHttpHeadersChildBuilderBase<ParentT, BuilderT>>
    extends ModelHttpHeadersBuilderBase<BuilderT> {
  private final ParentT parent;

  public ModelHttpHeadersChildBuilderBase(ParentT parent) {
    this(parent, emptyList());
  }

  public ModelHttpHeadersChildBuilderBase(ParentT parent, BuilderT that) {
    this(parent, that.headers());
  }

  public ModelHttpHeadersChildBuilderBase(ParentT parent, ModelHttpHeaders that) {
    this(parent, that.getHeaders());
  }

  public ModelHttpHeadersChildBuilderBase(ParentT parent, List<Header> headers) {
    super(headers);
    if (parent == null)
      throw new NullPointerException();
    this.parent = parent;
  }

  public ParentT done() {
    return parent;
  }
}
