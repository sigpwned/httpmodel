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

public final class ModelHttpEntities {
  private ModelHttpEntities() {}

  /**
   * From Section 4.3 of the HTTP/1.1 spec:
   *
   * For response messages, whether or not a message-body is included with a message is dependent on
   * both the request method and the response status code (section 6.1.1). All responses to the HEAD
   * request method MUST NOT include a message-body, even though the presence of entity- header
   * fields might lead one to believe they do. All 1xx (informational), 204 (no content), and 304
   * (not modified) responses MUST NOT include a message-body. All other responses do include a
   * message-body, although it MAY be of zero length.
   *
   * https://www.w3.org/Protocols/rfc2616/rfc2616-sec4.html#sec4.3
   */
  public static boolean responseEntityExists(String method, int statusCode) {
    boolean result;
    if (method.equalsIgnoreCase(ModelHttpMethods.HEAD)) {
      // No entity, by definition, even if headers say otherwise.
      result = false;
    } else if (statusCode / 100 == 1) {
      // Informational response codes have no response body
      result = false;
    } else if (statusCode == ModelHttpStatusCodes.NO_CONTENT) {
      // 204 No Content explicitly has no response entity by definition
      result = false;
    } else if (statusCode == ModelHttpStatusCodes.NOT_MODIFIED) {
      // 304 Not Modified has no response entity for caching
      result = false;
    } else {
      result = true;
    }
    return result;
  }
}
