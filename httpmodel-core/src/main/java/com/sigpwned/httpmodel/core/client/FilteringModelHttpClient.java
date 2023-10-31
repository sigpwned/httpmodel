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
package com.sigpwned.httpmodel.core.client;

import java.io.IOException;
import com.sigpwned.httpmodel.core.ModelHttpClient;
import com.sigpwned.httpmodel.core.model.ModelHttpRequest;
import com.sigpwned.httpmodel.core.model.ModelHttpResponse;

public class FilteringModelHttpClient implements ModelHttpClient {
  private final ModelHttpClient delegate;

  public FilteringModelHttpClient(ModelHttpClient delegate) {
    if (delegate == null)
      throw new NullPointerException();
    this.delegate = delegate;
  }

  @Override
  public ModelHttpResponse send(ModelHttpRequest originalRequest) throws IOException {
    final ModelHttpRequest filteredRequest = filterRequest(originalRequest);
    final ModelHttpResponse originalResponse = getDelegate().send(filteredRequest);
    final ModelHttpResponse filteredResponse = filterResponse(originalResponse);
    return filteredResponse;
  }

  /**
   * hook
   */
  protected ModelHttpRequest filterRequest(ModelHttpRequest request) throws IOException {
    return request;
  }

  /**
   * hook
   */
  protected ModelHttpResponse filterResponse(ModelHttpResponse response) throws IOException {
    return response;
  }

  @Override
  public void close() throws IOException {
    getDelegate().close();
  }

  private ModelHttpClient getDelegate() {
    return delegate;
  }
}
