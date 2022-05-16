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

public final class ModelHttpStatusCodes {
  private ModelHttpStatusCodes() {}

  /* 2XX: generally "OK" */

  /**
   * HTTP Status-Code 200: OK.
   */
  public static final int OK = 200;

  /**
   * HTTP Status-Code 201: Created.
   */
  public static final int CREATED = 201;

  /**
   * HTTP Status-Code 202: Accepted.
   */
  public static final int ACCEPTED = 202;

  /**
   * HTTP Status-Code 203: Non-Authoritative Information.
   */
  public static final int NOT_AUTHORITATIVE = 203;

  /**
   * HTTP Status-Code 204: No Content.
   */
  public static final int NO_CONTENT = 204;

  /**
   * HTTP Status-Code 205: Reset Content.
   */
  public static final int RESET = 205;

  /**
   * HTTP Status-Code 206: Partial Content.
   */
  public static final int PARTIAL = 206;

  /* 3XX: relocation/redirect */

  /**
   * HTTP Status-Code 300: Multiple Choices.
   */
  public static final int MULT_CHOICE = 300;

  /**
   * HTTP Status-Code 301: Moved Permanently.
   */
  public static final int MOVED_PERM = 301;

  /**
   * HTTP Status-Code 302: Temporary Redirect.
   */
  public static final int MOVED_TEMP = 302;

  /**
   * HTTP Status-Code 303: See Other.
   */
  public static final int SEE_OTHER = 303;

  /**
   * HTTP Status-Code 304: Not Modified.
   */
  public static final int NOT_MODIFIED = 304;

  /**
   * HTTP Status-Code 305: Use Proxy.
   */
  public static final int USE_PROXY = 305;

  /* 4XX: client error */

  /**
   * HTTP Status-Code 400: Bad Request.
   */
  public static final int BAD_REQUEST = 400;

  /**
   * HTTP Status-Code 401: Unauthorized.
   */
  public static final int UNAUTHORIZED = 401;

  /**
   * HTTP Status-Code 402: Payment Required.
   */
  public static final int PAYMENT_REQUIRED = 402;

  /**
   * HTTP Status-Code 403: Forbidden.
   */
  public static final int FORBIDDEN = 403;

  /**
   * HTTP Status-Code 404: Not Found.
   */
  public static final int NOT_FOUND = 404;

  /**
   * HTTP Status-Code 405: Method Not Allowed.
   */
  public static final int BAD_METHOD = 405;

  /**
   * HTTP Status-Code 406: Not Acceptable.
   */
  public static final int NOT_ACCEPTABLE = 406;

  /**
   * HTTP Status-Code 407: Proxy Authentication Required.
   */
  public static final int PROXY_AUTH = 407;

  /**
   * HTTP Status-Code 408: Request Time-Out.
   */
  public static final int CLIENT_TIMEOUT = 408;

  /**
   * HTTP Status-Code 409: Conflict.
   */
  public static final int CONFLICT = 409;

  /**
   * HTTP Status-Code 410: Gone.
   */
  public static final int GONE = 410;

  /**
   * HTTP Status-Code 411: Length Required.
   */
  public static final int LENGTH_REQUIRED = 411;

  /**
   * HTTP Status-Code 412: Precondition Failed.
   */
  public static final int PRECON_FAILED = 412;

  /**
   * HTTP Status-Code 413: Request Entity Too Large.
   */
  public static final int ENTITY_TOO_LARGE = 413;

  /**
   * HTTP Status-Code 414: Request-URI Too Large.
   */
  public static final int REQ_TOO_LONG = 414;

  /**
   * HTTP Status-Code 415: Unsupported Media Type.
   */
  public static final int UNSUPPORTED_TYPE = 415;

  /**
   * HTTP Status-Code 422: Unprocessable Entity
   */
  public static final int UNPROCESSABLE_ENTITY = 422;

  /**
   * HTTP Status-Code 429: Too Many Requests.
   */
  public static final int TOO_MANY_REQUESTS = 429;

  /**
   * HTTP Status-Code 451: Unavailable for Legal Reasons.
   */
  public static final int UNAVAILABLE_FOR_LEGAL_REASONS = 451;

  /* 5XX: server error */

  /**
   * HTTP Status-Code 500: Internal Server Error.
   */
  public static final int INTERNAL_ERROR = 500;

  /**
   * HTTP Status-Code 501: Not Implemented.
   */
  public static final int NOT_IMPLEMENTED = 501;

  /**
   * HTTP Status-Code 502: Bad Gateway.
   */
  public static final int BAD_GATEWAY = 502;

  /**
   * HTTP Status-Code 503: Service Unavailable.
   */
  public static final int UNAVAILABLE = 503;

  /**
   * HTTP Status-Code 504: Gateway Timeout.
   */
  public static final int GATEWAY_TIMEOUT = 504;

  /**
   * HTTP Status-Code 505: HTTP Version Not Supported.
   */
  public static final int VERSION = 505;
}
