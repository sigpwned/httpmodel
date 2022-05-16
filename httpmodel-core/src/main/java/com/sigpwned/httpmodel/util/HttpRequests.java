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

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import com.sigpwned.httpmodel.ModelHttpEntity;
import com.sigpwned.httpmodel.ModelHttpHeader;
import com.sigpwned.httpmodel.ModelHttpRequest;

/**
 * Converts model objects to and from the Java 11 built-in HTTP client implementation.
 */
public final class HttpRequests {
  private HttpRequests() {}

  /**
   * Converts the given request into an {@link HttpRequest} object. Does not send the request.
   * 
   * @param baseUrl The base URL to send the request to. The request only contains path and query
   *        string information, so a base URL is required. Example: https://en.wikipedia.org
   * @param request The request to convert
   * @return
   * @throws MalformedURLException
   */
  public static HttpRequest prepare(String baseUrl, ModelHttpRequest request)
      throws MalformedURLException {
    URI uri;
    try {
      uri = request.getUrl().toUrl(baseUrl).toURI();
    } catch (URISyntaxException e) {
      throw new MalformedURLException("Cannot convert URL to URI");
    }

    HttpRequest.Builder result = HttpRequest.newBuilder().uri(uri);

    for (ModelHttpHeader header : request.getHeaders())
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
}
