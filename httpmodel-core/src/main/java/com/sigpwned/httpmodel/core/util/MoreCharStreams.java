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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Helper methods for dealing with character streams.
 * 
 * @see Reader
 * @see Writer
 */
public final class MoreCharStreams {
  private MoreCharStreams() {}

  /**
   * Reads the data from the given {@link InputStream} and returns it as a byte array.
   * 
   * @throws IOException
   */
  public static String toString(Reader in) throws IOException {
    StringWriter out = new StringWriter();

    drain(in, out);

    return out.toString();
  }

  /**
   * Reads the data from the given {@link InputStream} and writes it to the given
   * {@link OutputStream}
   * 
   * @throws IOException
   */
  public static void drain(Reader in, Writer out) throws IOException {
    char[] buf = new char[4096];
    for (int nread = in.read(buf); nread != -1; nread = in.read(buf)) {
      out.write(buf, 0, nread);
    }
  }
}
