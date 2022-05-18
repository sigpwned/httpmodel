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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.sigpwned.httpmodel.ModelHttpEntity;
import com.sigpwned.httpmodel.ModelHttpHeader;
import com.sigpwned.httpmodel.ModelHttpMediaType;
import com.sigpwned.httpmodel.ModelHttpRequest;
import com.sigpwned.httpmodel.ModelHttpResponse;

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
   * @param baseUrl The base URL to send the request to. The request only contains path and query
   *        string information, so a base URL is required. Example: https://en.wikipedia.org
   * @param request The request to convert
   * @return
   * @throws MalformedURLException
   */
  public static HttpURLConnection fromRequest(String baseUrl, ModelHttpRequest request)
      throws IOException {
    URL url = request.getUrl().toUrl();

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

  /**
   * Converts a {@link HttpURLConnection} to a {@link ModelHttpResponse}. Note that this method will
   * make an HTTP request with the connection if the request has not already been made.
   * 
   * @throws IOException
   */
  public static ModelHttpResponse toResponse(HttpURLConnection cn) throws IOException {
    int statusCode = cn.getResponseCode();

    List<ModelHttpHeader> headers = new ArrayList<>();
    for (Map.Entry<String, List<String>> e : cn.getHeaderFields().entrySet()) {
      String headerName = e.getKey();
      List<String> headerValues = e.getValue();
      for (String headerValue : headerValues) {
        headers.add(ModelHttpHeader.of(headerName, headerValue));
      }
    }

    ModelHttpHeader contentTypeHeader =
        headers.stream().filter(h -> h.getName().equals(ModelHttpHeaderNames.CONTENT_TYPE))
            .findFirst().orElse(null);
    ModelHttpHeader contentLengthHeader =
        headers.stream().filter(h -> h.getName().equals(ModelHttpHeaderNames.CONTENT_LENGTH))
            .findFirst().orElse(null);
    ModelHttpHeader transferEncodingHeader =
        headers.stream().filter(h -> h.getName().equals(ModelHttpHeaderNames.TRANSFER_ENCODING))
            .findFirst().orElse(null);

    ModelHttpEntity entity;
    if (contentTypeHeader != null || contentLengthHeader != null
        || transferEncodingHeader != null) {
      ModelHttpMediaType contentType = Optional.ofNullable(contentTypeHeader)
          .map(ModelHttpHeader::getValue).map(ModelHttpMediaType::fromString)
          .orElse(ModelHttpMediaTypes.APPLICATION_OCTET_STREAM);

      byte[] data;
      try (InputStream in = cn.getInputStream()) {
        data = MoreByteStreams.toByteArray(in);
      }

      entity = ModelHttpEntity.of(contentType, data);
    } else {
      entity = null;
    }

    return ModelHttpResponse.of(statusCode, headers, entity);
  }
}
