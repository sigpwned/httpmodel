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
 * Models an HTTP request
 */
public abstract class ModelHttpResponseHeadBuilderBase<HeadersBuilderT extends ModelHttpHeadersBuilderBase<HeadersBuilderT>, BuilderT extends ModelHttpResponseHeadBuilderBase<HeadersBuilderT, BuilderT>> {
  private int statusCode;

  private HeadersBuilderT headers;

  public ModelHttpResponseHeadBuilderBase() {}

  public ModelHttpResponseHeadBuilderBase(BuilderT that) {
    this.statusCode = that.statusCode();
    this.headers = that.headers();
  }

  public ModelHttpResponseHeadBuilderBase(ModelHttpResponseHead that) {
    this.statusCode = that.getStatusCode();
    this.headers = newHeadersBuilder().assign(that.getHeaders());
  }

  public int statusCode() {
    return statusCode;
  }

  @SuppressWarnings("unchecked")
  public BuilderT statusCode(int statusCode) {
    this.statusCode = statusCode;
    return (BuilderT) this;
  }

  public HeadersBuilderT headers() {
    return headers;
  }

  @SuppressWarnings("unchecked")
  public BuilderT headers(HeadersBuilderT headers) {
    this.headers = headers;
    return (BuilderT) this;
  }

  @SuppressWarnings("unchecked")
  public BuilderT headers(ModelHttpHeaders newHeaders) {
    if (headers == null)
      headers = newHeadersBuilder();
    headers.assign(newHeaders);
    return (BuilderT) this;
  }

  public ModelHttpResponseHead build() {
    return new ModelHttpResponseHead(statusCode(), headers().build());
  }

  protected abstract HeadersBuilderT newHeadersBuilder();
}
