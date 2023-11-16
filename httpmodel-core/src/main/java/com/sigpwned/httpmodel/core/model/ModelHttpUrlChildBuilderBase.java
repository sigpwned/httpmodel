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

public abstract class ModelHttpUrlChildBuilderBase<ParentT, QueryStringBuilderT extends ModelHttpQueryStringBuilderBase<QueryStringBuilderT>, BuilderT extends ModelHttpUrlBuilderBase<QueryStringBuilderT, BuilderT>>
    extends ModelHttpUrlBuilderBase<QueryStringBuilderT, BuilderT> {
  private final ParentT parent;

  public ModelHttpUrlChildBuilderBase(ParentT parent) {
    this(parent, null, null, null, null);
  }

  public ModelHttpUrlChildBuilderBase(ParentT parent, BuilderT that) {
    this(parent, that.scheme(), that.authority(), that.path(), that.queryString());
  }

  public ModelHttpUrlChildBuilderBase(ParentT parent, String scheme, ModelHttpAuthority authority,
      String path, QueryStringBuilderT queryString) {
    super(scheme, authority, path, queryString);
    if (parent == null)
      throw new NullPointerException();
    this.parent = parent;
  }

  public ParentT done() {
    return parent;
  }
}
