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
import java.net.HttpURLConnection;
import com.sigpwned.httpmodel.core.ModelHttpClient;
import com.sigpwned.httpmodel.core.model.ModelHttpRequest;
import com.sigpwned.httpmodel.core.model.ModelHttpResponse;
import com.sigpwned.httpmodel.core.util.ModelHttpURLConnections;

public class UrlConnectionModelHttpClient implements ModelHttpClient {
  private boolean closed;

  public UrlConnectionModelHttpClient() {
    this.closed = false;
  }

  @Override
  public ModelHttpResponse send(ModelHttpRequest request) throws IOException {
    if (closed)
      throw new IOException("closed");
    ModelHttpResponse result;
    HttpURLConnection cn = ModelHttpURLConnections.toRequest(request);
    try {
      result = ModelHttpURLConnections.fromResponse(cn);
    } finally {
      cn.disconnect();
    }
    return result;
  }

  @Override
  public void close() throws IOException {
    closed = true;
  }
}