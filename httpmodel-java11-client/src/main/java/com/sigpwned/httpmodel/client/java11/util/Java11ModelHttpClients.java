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
package com.sigpwned.httpmodel.client.java11.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.sigpwned.httpmodel.core.io.InputStreamBufferingStrategy;
import com.sigpwned.httpmodel.core.model.ModelHttpHeaders;
import com.sigpwned.httpmodel.core.model.ModelHttpRequest;
import com.sigpwned.httpmodel.core.model.ModelHttpResponse;
import com.sigpwned.httpmodel.core.util.ModelHttpHeaderNames;

/**
 * Converts model objects to and from the Java 11 built-in HTTP client implementation. This is for
 * the client side.
 */
public final class Java11ModelHttpClients {
  private Java11ModelHttpClients() {}

  /**
   * Converts the given request into an {@link HttpRequest} object. Does not send the request. If
   * the request has not already been buffered, then this method buffers it using the default
   * buffering strategy.
   *
   * @throws IOException
   *
   * @see ModelHttpRequest#buffer(InputStreamBufferingStrategy)
   * @see InputStreamBufferingStrategy#DEFAULT
   */
  public static HttpRequest toRequest(ModelHttpRequest request) throws IOException {
    URI uri;
    try {
      uri = request.getUrl().toUrl().toURI();
    } catch (URISyntaxException e) {
      throw new MalformedURLException("Cannot convert URL to URI");
    }

    HttpRequest.Builder result = HttpRequest.newBuilder().uri(uri);

    for (ModelHttpHeaders.Header header : request.getHeaders())
      result.header(header.getName(), header.getValue());

    if (request.hasEntity()) {
      if (!request.isBuffered())
        request.buffer(InputStreamBufferingStrategy.DEFAULT);
      request.getContentType().ifPresent(contentType -> {
        result.setHeader(ModelHttpHeaderNames.CONTENT_TYPE, contentType.toString());
      });
      try {
        result.method(request.getMethod(), BodyPublishers.ofInputStream(() -> {
          try {
            request.restart();
          } catch (IOException e) {
            throw new UncheckedIOException(e);
          }
          return request;
        }));
      } catch (UncheckedIOException e) {
        throw e.getCause();
      }
    } else {
      result.method(request.getMethod(), BodyPublishers.noBody());
    }

    return result.build();
  }

  /**
   * Converts an {@link HttpResponse} to a {@link ModelHttpResponse}.
   *
   * @throws IOException
   */
  public static ModelHttpResponse fromResponse(HttpResponse<InputStream> response)
      throws IOException {
    int statusCode = response.statusCode();

    List<ModelHttpHeaders.Header> headers = new ArrayList<>();
    for (Map.Entry<String, List<String>> e : response.headers().map().entrySet()) {
      String headerName = e.getKey();
      List<String> headerValues = e.getValue();
      for (String headerValue : headerValues) {
        headers.add(ModelHttpHeaders.Header.of(headerName, headerValue));
      }
    }

    ModelHttpHeaders.Header contentTypeHeader =
        headers.stream().filter(h -> h.getName().equals(ModelHttpHeaderNames.CONTENT_TYPE))
            .findFirst().orElse(null);
    ModelHttpHeaders.Header contentLengthHeader =
        headers.stream().filter(h -> h.getName().equals(ModelHttpHeaderNames.CONTENT_LENGTH))
            .findFirst().orElse(null);
    ModelHttpHeaders.Header transferEncodingHeader =
        headers.stream().filter(h -> h.getName().equals(ModelHttpHeaderNames.TRANSFER_ENCODING))
            .findFirst().orElse(null);

    InputStream entity;
    if (contentTypeHeader != null || contentLengthHeader != null
        || transferEncodingHeader != null) {
      entity = response.body();
    } else {
      entity = null;
    }

    return new ModelHttpResponse(statusCode, new ModelHttpHeaders(headers), entity);
  }
}
