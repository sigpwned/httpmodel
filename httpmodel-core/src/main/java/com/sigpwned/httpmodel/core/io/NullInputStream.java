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
package com.sigpwned.httpmodel.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.OptionalLong;

public class NullInputStream extends BufferedInputStream {
  public NullInputStream() {
    in = newInputStream();
  }

  @Override
  public int read() throws IOException {
    return -1;
  }

  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    return -1;
  }

  @Override
  public int read(byte[] b) throws IOException {
    return -1;
  }

  @Override
  public OptionalLong length() throws IOException {
    return OptionalLong.of(0L);
  }

  @Override
  public void close() throws IOException {
    // meh
  }

  /**
   * hook
   */
  @Override
  protected InputStream newInputStream() {
    return this;
  }
}
