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
package com.sigpwned.httpmodel.client;

import java.io.IOException;
import com.sigpwned.httpmodel.core.model.ModelHttpRequest;
import com.sigpwned.httpmodel.core.model.ModelHttpResponse;

/**
 * <p>
 * Performs a synchronous, round-trip HTTP request.
 * </p>
 *
 * <p>
 * Each request performs the following steps in the following order before returning the response:
 * </p>
 *
 * <ol>
 * <li>Run request filters</li>
 * <li>Run request interceptors</li>
 * <li>Send request and receive response</li>
 * <li>Run response filters</li>
 * <li>Run response interceptors</li>
 * </ol>
 *
 * <p>
 * Individual implementations may include additional steps.
 * </p>
 */
public interface ModelHttpClient extends AutoCloseable {
  public ModelHttpResponse send(ModelHttpRequest request) throws IOException;

  public void addRequestFilter(ModelHttpRequestFilter requestFilter);

  public void removeRequestFilter(ModelHttpRequestFilter requestFilter);

  public void addRequestInterceptor(ModelHttpRequestInterceptor requestInterceptor);

  public void removeRequestInterceptor(ModelHttpRequestInterceptor requestInterceptor);

  public void addResponseFilter(ModelHttpResponseFilter responseFilter);

  public void removeResponseFilter(ModelHttpResponseFilter responseFilter);

  public void addResponseInterceptor(ModelHttpResponseInterceptor responseInterceptor);

  public void removeResponseInterceptor(ModelHttpResponseInterceptor responseInterceptor);

  @Override
  default void close() throws IOException {}
}
