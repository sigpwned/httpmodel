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

import java.io.FileNotFoundException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.OptionalLong;

public abstract class BufferedInputStream extends FilterInputStream {
  public BufferedInputStream() {
    super(null);
  }

  /**
   * Re-starts this stream from its beginning. If the stream was previously closed, then it can now
   * be read until it is closed again.
   *
   * @see #close()
   */
  public void restart() throws IOException {
    InputStream newInputStream = newInputStream();
    if (newInputStream == null)
      throw new NullPointerException();
    in.close();
    in = newInputStream;
  }

  /**
   * total bytes, not remaining
   */
  public abstract OptionalLong length() throws IOException;

  /**
   * hook
   *
   * @return An {@link InputStream} containing the same bytes as the original argument to the
   *         constructor. Must not be {@code null}.
   * @throws FileNotFoundException
   */
  protected abstract InputStream newInputStream() throws IOException;
}
