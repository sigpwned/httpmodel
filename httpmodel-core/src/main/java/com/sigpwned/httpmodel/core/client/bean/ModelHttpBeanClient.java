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
package com.sigpwned.httpmodel.core.client.bean;

import java.io.IOException;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;
import com.sigpwned.httpmodel.core.model.ModelHttpUrl;

/**
 * <p>
 * Performs a synchronous, round-trip HTTP request with bean mapping on request and response.
 * </p>
 *
 * <p>
 * Each request performs the following steps in the following order before returning the response:
 * </p>
 *
 * <ol>
 * <li>Run request filters</li>
 * <li>Map request bean to HTTP request entity</li>
 * <li>Run request interceptors</li>
 * <li>Send request and receive response</li>
 * <li>Run response filters</li>
 * <li>Run response interceptors</li>
 * <li>Run exception mappers</li>
 * <li>Map HTTP response entity to response bean</li>
 * </ol>
 *
 * <p>
 * Individual implementations may include additional steps.
 * </p>
 */
public interface ModelHttpBeanClient extends AutoCloseable {
  default <RequestT, ResponseT> ResponseT send(String method, ModelHttpUrl url, RequestT request,
      Class<ResponseT> responseType) throws IOException {
    return send(ModelHttpRequestHead.builder().url(url).method(method).build(), request,
        responseType);
  }

  public <RequestT, ResponseT> ResponseT send(ModelHttpRequestHead requestHead, RequestT request,
      Class<ResponseT> responseType) throws IOException;

  public void registerRequestMapper(ModelHttpBeanClientRequestMapper<?> requestMapper);

  public void registerResponseMapper(ModelHttpBeanClientResponseMapper<?> responseMapper);

  public void registerExceptionMapper(ModelHttpBeanClientExceptionMapper exceptionMapper);

  @Override
  public void close() throws IOException;
}
