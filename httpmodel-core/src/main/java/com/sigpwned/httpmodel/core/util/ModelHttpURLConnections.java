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
package com.sigpwned.httpmodel.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.sigpwned.httpmodel.core.model.ModelHttpHeaders;
import com.sigpwned.httpmodel.core.model.ModelHttpRequest;
import com.sigpwned.httpmodel.core.model.ModelHttpResponse;

/**
 * Converts model objects to and from the Java 11 built-in HTTP client implementation. This is for
 * the client side.
 */
public final class ModelHttpURLConnections {
  private ModelHttpURLConnections() {}

  /**
   * Converts and sends the given request using the built-in {@link URLConnection} classes. Note
   * that this method may send the given request, which is unavoidable because a request body is
   * potentially included.
   *
   * @throws MalformedURLException
   */
  public static HttpURLConnection toRequest(ModelHttpRequest request) throws IOException {
    URL url = request.getUrl().toUrl();

    HttpURLConnection result = (HttpURLConnection) url.openConnection();

    result.setRequestMethod(request.getMethod());

    for (ModelHttpHeaders.Header header : request.getHeaders())
      result.setRequestProperty(header.getName(), header.getValue());

    if (request.hasEntity()) {
      request.getContentType().ifPresent(contentType -> {
        result.setRequestProperty(ModelHttpHeaderNames.CONTENT_TYPE, contentType.toString());
      });

      result.setDoOutput(true);

      try (OutputStream out = result.getOutputStream()) {
        MoreByteStreams.drain(request, out);
      }
    } else {
      result.setDoOutput(false);
    }

    return result;
  }

  /**
   * Converts a {@link HttpURLConnection} to a {@link ModelHttpResponse}. Note that this method will
   * make an HTTP request with the connection if the request has not already been made.
   *
   * @throws IOException
   */
  public static ModelHttpResponse fromResponse(HttpURLConnection cn) throws IOException {
    int statusCode = cn.getResponseCode();

    List<ModelHttpHeaders.Header> headers = new ArrayList<>();
    for (Map.Entry<String, List<String>> e : cn.getHeaderFields().entrySet()) {
      String headerName = e.getKey();

      // HttpURLConnection objects expose their response line as a header, for reasons passing
      // understanding. We can recognized it by the null headerName value.
      if (headerName == null)
        continue;

      List<String> headerValues = e.getValue();
      for (String headerValue : headerValues) {
        headers.add(ModelHttpHeaders.Header.of(headerName, headerValue));
      }
    }

    InputStream entity;
    if (ModelHttpEntities.responseEntityExists(cn.getRequestMethod(), statusCode)) {
      if (statusCode / 100 == 2) {
        entity = cn.getInputStream();
      } else {
        entity = cn.getErrorStream();
      }
    } else {
      entity = null;
    }

    return new ModelHttpResponse(statusCode, ModelHttpHeaders.of(headers), entity);
  }
}
