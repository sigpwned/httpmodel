/*-
 * =================================LICENSE_START==================================
 * httpmodel-java11-client
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
package com.sigpwned.httpmodel.client.java11;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import com.sigpwned.httpmodel.client.java11.util.Java11ModelHttpClients;
import com.sigpwned.httpmodel.core.client.ModelHttpConnector;
import com.sigpwned.httpmodel.core.model.ModelHttpRequest;
import com.sigpwned.httpmodel.core.model.ModelHttpResponse;

public class Java11ModelHttpClient implements ModelHttpConnector {
  public static HttpClient defaultHttpClient() {
    return HttpClient.newHttpClient();
  }

  private final HttpClient client;

  public Java11ModelHttpClient() {
    this(defaultHttpClient());
  }

  public Java11ModelHttpClient(HttpClient client) {
    if (client == null)
      throw new NullPointerException();
    this.client = client;
  }

  @Override
  public ModelHttpResponse send(ModelHttpRequest modelRequest) throws IOException {
    HttpRequest java11Request = Java11ModelHttpClients.toRequest(modelRequest);

    HttpResponse<InputStream> java11Response;
    try {
      java11Response = getClient().send(java11Request, BodyHandlers.ofInputStream());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new InterruptedIOException("interrupted");
    }

    ModelHttpResponse modelResponse = transformResponse(java11Response);

    return modelResponse;
  }

  /**
   * hook
   */
  protected HttpRequest transformRequest(ModelHttpRequest modelRequest) throws IOException {
    return Java11ModelHttpClients.toRequest(modelRequest);
  }

  /**
   * hook
   */
  protected ModelHttpResponse transformResponse(HttpResponse<InputStream> java11Response)
      throws IOException {
    return Java11ModelHttpClients.fromResponse(java11Response);
  }

  @Override
  public void close() throws IOException {
    // NOP
  }

  private HttpClient getClient() {
    return client;
  }
}
