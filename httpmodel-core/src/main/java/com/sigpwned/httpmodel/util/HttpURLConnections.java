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
package com.sigpwned.httpmodel.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import com.sigpwned.httpmodel.ModelHttpEntity;
import com.sigpwned.httpmodel.ModelHttpHeader;
import com.sigpwned.httpmodel.ModelHttpRequest;

/**
 * Converts model objects to and from the Java 11 built-in HTTP client implementation.
 */
public final class HttpURLConnections {
  private HttpURLConnections() {}

  /**
   * Converts and sends the given request using the built-in {@link URLConnection} classes. Note
   * that this method may send the given request, which is unavoidable because a request body is
   * potentially included.
   * 
   * @param baseUrl The base URL to send the request to. The request only contains path and query
   *        string information, so a base URL is required. Example: https://en.wikipedia.org
   * @param request The request to convert
   * @return
   * @throws MalformedURLException
   */
  public static HttpURLConnection prepare(String baseUrl, ModelHttpRequest request)
      throws IOException {
    URL url = request.getUrl().toUrl(baseUrl);

    HttpURLConnection result = (HttpURLConnection) url.openConnection();

    result.setRequestMethod(request.getMethod());

    for (ModelHttpHeader header : request.getHeaders())
      result.setRequestProperty(header.getName(), header.getValue());

    if (request.getEntity().isPresent()) {
      ModelHttpEntity entity = request.getEntity().get();

      result.setRequestProperty(ModelHttpHeaderNames.CONTENT_TYPE, entity.getType().toString());

      result.setDoOutput(true);

      try (OutputStream out = result.getOutputStream()) {
        try (InputStream in = entity.readBytes()) {
          MoreByteStreams.drain(in, out);
        }
      }
    } else {
      result.setDoOutput(false);
    }

    return result;
  }
}
