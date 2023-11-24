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
package com.sigpwned.httpmodel.client.impl;

import java.io.IOException;
import com.sigpwned.httpmodel.client.ModelHttpClient;
import com.sigpwned.httpmodel.client.ModelHttpConnector;
import com.sigpwned.httpmodel.core.model.ModelHttpRequest;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;
import com.sigpwned.httpmodel.core.model.ModelHttpResponse;
import com.sigpwned.httpmodel.core.model.ModelHttpResponseHead;

/**
 * <p>
 * Performs a synchronous, round-trip HTTP request. Uses the standard set of steps as documented on
 * {@link ModelHttpClient}.
 * </p>
 *
 * @see ModelHttpClient
 */
public class DefaultModelHttpClient extends DefaultModelHttpClientBase implements ModelHttpClient {
  public DefaultModelHttpClient(ModelHttpConnector connector) {
    super(connector);
  }

  @Override
  public ModelHttpResponse send(ModelHttpRequest request) throws IOException {
    doRequestFilters(ModelHttpRequestHead.fromRequest(request));

    doRequestInterceptors(request);

    ModelHttpResponse response = doSend(request);

    doResponseFilters(ModelHttpResponseHead.fromResponse(response));

    doResponseInterceptors(response);

    return response;
  }

  /**
   * hook
   *
   * @param request
   * @return
   * @throws IOException
   */
  protected ModelHttpResponse doSend(ModelHttpRequest request) throws IOException {
    return getConnector().send(request);
  }
}
