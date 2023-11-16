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
package com.sigpwned.httpmodel.core.io.buffered;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.OptionalLong;
import com.sigpwned.httpmodel.core.io.BufferedInputStream;

public class MemoryBufferedInputStream extends BufferedInputStream {
  public static MemoryBufferedInputStream of(String s, Charset charset) {
    return new MemoryBufferedInputStream(s, charset);
  }

  public static MemoryBufferedInputStream of(byte[] bytes) {
    return new MemoryBufferedInputStream(bytes);
  }

  public static MemoryBufferedInputStream of(byte[] bytes, int off, int len) {
    return new MemoryBufferedInputStream(bytes, off, len);
  }

  private final byte[] bytes;
  private final int off;
  private final int len;

  public MemoryBufferedInputStream(String s, Charset charset) {
    this(s.getBytes(charset));
  }

  public MemoryBufferedInputStream(byte[] bytes) {
    this(bytes, 0, bytes.length);
  }

  public MemoryBufferedInputStream(byte[] bytes, int off, int len) {
    this.bytes = bytes;
    this.off = off;
    this.len = len;
    in = newInputStream();
  }

  @Override
  public OptionalLong length() throws IOException {
    return OptionalLong.of(len);
  }

  @Override
  public void close() throws IOException {
    try {
      super.close();
    } finally {
      // No cleanup to do
    }
  }

  /**
   * hook
   */
  @Override
  protected InputStream newInputStream() {
    return new ByteArrayInputStream(bytes, off, len);
  }
}
