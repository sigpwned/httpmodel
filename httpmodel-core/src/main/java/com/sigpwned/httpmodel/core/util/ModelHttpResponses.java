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
package com.sigpwned.httpmodel.core.util;

import java.io.IOException;
import java.util.Objects;
import com.sigpwned.httpmodel.core.model.ModelHttpResponse;

public final class ModelHttpResponses {
  private ModelHttpResponses() {}

  public static int hashCode(ModelHttpResponse httpResponse) throws IOException {
    return Objects.hash(httpResponse.getHeaders(), httpResponse.getStatusCode(),
        EntityInputStreams.hashCode(httpResponse));
  }

  public static boolean equals(ModelHttpResponse a, ModelHttpResponse b) throws IOException {
    if (a == b)
      return true;
    if (a == null || b == null)
      return false;
    if (a.getClass() != b.getClass())
      return false;
    return Objects.equals(a.getHeaders(), b.getHeaders()) && a.getStatusCode() == b.getStatusCode()
        && EntityInputStreams.contentEquals(a, b);
  }
}
