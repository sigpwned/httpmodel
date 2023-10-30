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
package com.sigpwned.httpmodel.core.util;

import com.sigpwned.httpmodel.core.ModelHttpMediaType;

/**
 * Constants for common MIME content types
 */
public final class ModelHttpMediaTypes {
  private ModelHttpMediaTypes() {}

  public static final ModelHttpMediaType ANY = ModelHttpMediaType.of("*", "*");

  public static final ModelHttpMediaType ANY_IMAGE = ModelHttpMediaType.of("image", "*");

  public static final ModelHttpMediaType APPLICATION_CSV =
      ModelHttpMediaType.of("application", "csv");

  public static final ModelHttpMediaType APPLICATION_JSON =
      ModelHttpMediaType.of("application", "json");

  public static final ModelHttpMediaType APPLICATION_OCTET_STREAM =
      ModelHttpMediaType.of("application", "octet-stream");

  public static final ModelHttpMediaType APPLICATION_X_WWW_FORM_URLENCODED =
      ModelHttpMediaType.of("application", "x-www-form-urlencoded");

  public static final ModelHttpMediaType TEXT_PLAIN = ModelHttpMediaType.of("text", "plain");
}
