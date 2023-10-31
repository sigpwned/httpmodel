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

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.sigpwned.httpmodel.core.model.ModelHttpEntity;
import com.sigpwned.httpmodel.core.model.ModelHttpHeaders;
import com.sigpwned.httpmodel.core.model.ModelHttpMediaType;
import com.sigpwned.httpmodel.core.model.ModelHttpRequest;
import com.sigpwned.httpmodel.core.model.ModelHttpResponse;
import com.sigpwned.httpmodel.core.util.ModelHttpHeaderNames;
import com.sigpwned.httpmodel.core.util.ModelHttpMediaTypes;

/**
 * Converts model objects to and from the Java 11 built-in HTTP client implementation. This is for
 * the client side.
 */
public final class Java11ModelHttpClients {
  private Java11ModelHttpClients() {}

  /**
   * Converts the given request into an {@link HttpRequest} object. Does not send the request.
   * 
   * @throws MalformedURLException
   */
  public static HttpRequest toRequest(ModelHttpRequest request) throws MalformedURLException {
    URI uri;
    try {
      uri = request.getUrl().toUrl().toURI();
    } catch (URISyntaxException e) {
      throw new MalformedURLException("Cannot convert URL to URI");
    }

    HttpRequest.Builder result = HttpRequest.newBuilder().uri(uri);

    for (ModelHttpHeaders.Header header : request.getHeaders())
      result.header(header.getName(), header.getValue());

    if (request.getEntity().isPresent()) {
      ModelHttpEntity entity = request.getEntity().get();
      result.method(request.getMethod(), BodyPublishers.ofByteArray(entity.toByteArray()));
      if (entity.getType().isPresent())
        result.setHeader(ModelHttpHeaderNames.CONTENT_TYPE, entity.getType().toString());
    } else {
      result.method(request.getMethod(), BodyPublishers.noBody());
    }

    return result.build();
  }

  /**
   * Converts an {@link HttpResponse} to a {@link ModelHttpResponse}.
   */
  public static ModelHttpResponse fromResponse(HttpResponse<byte[]> response) {
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

    ModelHttpEntity entity;
    if (contentTypeHeader != null || contentLengthHeader != null
        || transferEncodingHeader != null) {
      ModelHttpMediaType contentType = Optional.ofNullable(contentTypeHeader)
          .map(ModelHttpHeaders.Header::getValue).map(ModelHttpMediaType::fromString)
          .orElse(ModelHttpMediaTypes.APPLICATION_OCTET_STREAM);
      entity = ModelHttpEntity.of(contentType, response.body());
    } else {
      entity = null;
    }

    return ModelHttpResponse.of(statusCode, ModelHttpHeaders.of(headers), entity);
  }
}
