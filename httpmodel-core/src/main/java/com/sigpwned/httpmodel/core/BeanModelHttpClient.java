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
package com.sigpwned.httpmodel.core;

import java.io.IOException;
import com.sigpwned.httpmodel.core.model.ModelHttpRequest;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestBuilder;
import com.sigpwned.httpmodel.core.model.ModelHttpUrl;

public interface BeanModelHttpClient extends AutoCloseable {

  default <RequestT, ResponseT> ResponseT send(ModelHttpUrl url, String method, RequestT request,
      Class<ResponseT> responseType) throws IOException {
    return send(ModelHttpRequest.builder().url(url).method(method), request, responseType);
  }

  public <RequestT, ResponseT> ResponseT send(ModelHttpRequestBuilder httpRequestBuilder,
      RequestT request, Class<ResponseT> responseType) throws IOException;

  @Override
  public void close() throws IOException;
}
