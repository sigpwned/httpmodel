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
package com.sigpwned.httpmodel.core.model;

import com.sigpwned.httpmodel.core.io.BufferedInputStream;

/**
 * Standalone HTTP entity without built-in lifecycle controls. Good fit for entities that don't
 * require lifecycle management (e.g., in-memory entities), or entities with externally-managed
 * lifecycle (e.g., temporary files deleted on exit).
 */
public interface ModelHttpEntity {
  public abstract ModelHttpMediaType getContentType();

  /**
   * Prefer for underlying stream to be bufferable.
   */
  public abstract BufferedInputStream toInputStream();
}
