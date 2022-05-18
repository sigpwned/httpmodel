/*-
 * =================================LICENSE_START==================================
 * httpmodel-servlet
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
package com.sigpwned.httpmodel.servlet.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.sigpwned.httpmodel.ModelHttpAuthority;
import com.sigpwned.httpmodel.ModelHttpEntity;
import com.sigpwned.httpmodel.ModelHttpHeaders;
import com.sigpwned.httpmodel.ModelHttpHost;
import com.sigpwned.httpmodel.ModelHttpMediaType;
import com.sigpwned.httpmodel.ModelHttpQueryString;
import com.sigpwned.httpmodel.ModelHttpRequest;
import com.sigpwned.httpmodel.ModelHttpResponse;
import com.sigpwned.httpmodel.ModelHttpUrl;
import com.sigpwned.httpmodel.util.ModelHttpHeaderNames;
import com.sigpwned.httpmodel.util.ModelHttpMediaTypes;
import com.sigpwned.httpmodel.util.MoreByteStreams;

/**
 * This is server side.
 */
public final class ModelHttpServlets {
  private ModelHttpServlets() {}

  /**
   * Converts the given {@link HttpServletRequest} into a {@link ModelHttpRequest}.
   */
  public static ModelHttpRequest toModelRequest(HttpServletRequest request) throws IOException {
    ModelHttpQueryString queryString = Optional.ofNullable(request.getQueryString())
        .map(ModelHttpQueryString::fromString).orElse(null);

    ModelHttpUrl url = ModelHttpUrl.of(
        request.getScheme(), ModelHttpAuthority
            .of(ModelHttpHost.fromString(request.getServerName()), request.getServerPort()),
        request.getRequestURI(), queryString);

    String method = request.getMethod().toUpperCase();

    String version = request.getProtocol();

    List<ModelHttpHeaders.Header> headers = new ArrayList<>();

    Enumeration<String> headerNames = request.getHeaderNames();
    while (headerNames.hasMoreElements()) {
      String headerName = headerNames.nextElement();
      Enumeration<String> headerValues = request.getHeaders(headerName);
      while (headerValues.hasMoreElements()) {
        String headerValue = headerValues.nextElement();
        headers.add(ModelHttpHeaders.Header.of(headerName.toLowerCase(), headerValue));
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
      ModelHttpMediaType type =
          headers.stream().filter(h -> h.getName().equals(ModelHttpHeaderNames.CONTENT_TYPE))
              .map(ModelHttpHeaders.Header::getValue).map(ModelHttpMediaType::fromString)
              .findFirst().orElse(null);

      byte[] entityBody;
      try (InputStream in = request.getInputStream()) {
        entityBody = MoreByteStreams.toByteArray(in);
      }

      entity = ModelHttpEntity.of(
          Optional.ofNullable(type).orElse(ModelHttpMediaTypes.APPLICATION_OCTET_STREAM),
          entityBody);
    } else {
      entity = null;
    }

    return ModelHttpRequest.of(version, method, url, ModelHttpHeaders.of(headers), entity);
  }

  /**
   * Fills in the given {@link HttpServletResponse} from the given {@link ModelHttpResponse}
   * 
   * @throws IOException
   */
  public static HttpServletResponse fromModelResponse(HttpServletResponse result,
      ModelHttpResponse response) throws IOException {
    result.setStatus(response.getStatusCode());

    for (ModelHttpHeaders.Header header : response.getHeaders()) {
      // TODO Encode headers?
      // See: https://stackoverflow.com/questions/324470/http-headers-encoding-decoding-in-java
      result.addHeader(header.getName(), header.getValue());
    }

    if (response.getEntity().isPresent()) {
      ModelHttpEntity entity = response.getEntity().get();

      result.setContentType(
          entity.getType().orElse(ModelHttpMediaTypes.APPLICATION_OCTET_STREAM).toString());

      result.setContentLength(entity.length());

      try (OutputStream out = result.getOutputStream()) {
        try (InputStream in = entity.readBytes()) {
          MoreByteStreams.drain(in, out);
        }
      }
    }

    return result;
  }
}
