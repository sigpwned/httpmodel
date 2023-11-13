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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Helper methods for dealing with byte streams
 *
 * @see InputStream
 * @see OutputStream
 */
public final class MoreByteStreams {
  private MoreByteStreams() {}

  /**
   * Reads the data from the given {@link InputStream} and returns it as a byte array.
   *
   * @throws IOException
   */
  public static byte[] toByteArray(InputStream in) throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    drain(in, out);

    return out.toByteArray();
  }

  /**
   * Reads the data from the given {@link InputStream} and writes it to the given
   * {@link OutputStream}
   *
   * @throws IOException
   */
  public static void drain(InputStream in, OutputStream out) throws IOException {
    byte[] buf = new byte[4096];
    for (int nread = in.read(buf); nread != -1; nread = in.read(buf)) {
      out.write(buf, 0, nread);
    }
  }

  /**
   * Equivalent to {@code read(in, buf, 0, buf.length)}
   *
   * @see #read(InputStream, byte[], int, int)
   */
  public static int read(InputStream in, byte[] buf) throws IOException {
    return read(in, buf, 0, buf.length);
  }

  /**
   * Read as many bytes from {@code in} as possible, up to {@code len}, and store them in
   * {@code buf} starting at offset {@code off}.
   *
   * @param in The {@link InputStream} from which to read data
   * @param buf The buffer in which to store data
   * @param off The offset in {@code buf} at which to store data
   * @param len The maximum number of bytes to read from {@code in} and store in {@code buf}
   * @return The actual number of bytes read. Always less than or equal to {@code len}.
   * @throws IOException
   */
  public static int read(InputStream in, byte[] buf, int off, int len) throws IOException {
    int totread = 0;

    int nread = in.read(buf, off + totread, len - totread);
    while (nread != -1) {
      totread = totread + nread;
      if (totread == len)
        break;
      nread = in.read(buf, off + totread, len - totread);
    }

    return totread;
  }
}
