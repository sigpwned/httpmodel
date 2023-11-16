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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.sigpwned.httpmodel.core.io.buffered.FileBufferedInputStream;
import com.sigpwned.httpmodel.core.io.buffered.MemoryBufferedInputStream;
import com.sigpwned.httpmodel.core.util.MoreByteStreams;

@FunctionalInterface
public interface InputStreamBufferer {
  public static final int DEFAULT_MAX_BUFFER_SIZE = 64 * 1024;

  /**
   * Equivalent to {@code newDefaultInstance(DEFAULT_MAX_BUFFER_SIZE)}.
   */
  public static InputStreamBufferer newDefaultInstance() {
    return newDefaultInstance(DEFAULT_MAX_BUFFER_SIZE);
  }

  /**
   * If the given {@link InputStream} contains fewer than {code maxBufferSize} bytes, then returns a
   * {@link MemoryBufferedInputStream}. Otherwise, writes the contents of the stream to a temporary
   * file and returns a {@link FileBufferedInputStream}. The temporary file is deleted automatically
   * when the given stream is closed.
   *
   * @see File#createTempFile(String, String)
   */
  public static InputStreamBufferer newDefaultInstance(int maxBufferSize) {
    return new InputStreamBufferer() {
      @Override
      public BufferedInputStream buffer(InputStream in) throws IOException {
        byte[] buf = new byte[maxBufferSize];
        int nread = MoreByteStreams.read(in, buf);
        if (nread == 0)
          return new NullInputStream();
        if (nread < buf.length)
          return new MemoryBufferedInputStream(buf, 0, nread);

        BufferedInputStream result = null;
        File tmp = File.createTempFile("input.", ".buf");
        try {
          try (OutputStream out = new FileOutputStream(tmp)) {
            out.write(buf, 0, nread);
            MoreByteStreams.drain(in, out);
          }
          result = new FileBufferedInputStream(tmp, true);
        } finally {
          if (result == null)
            tmp.delete();
        }

        return result;
      }
    };
  }

  public static InputStreamBufferer newMemoryInstance() {
    return new InputStreamBufferer() {
      @Override
      public BufferedInputStream buffer(InputStream in) throws IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        try {
          MoreByteStreams.drain(in, buf);
        } finally {
          buf.close();
        }
        return new MemoryBufferedInputStream(buf.toByteArray());
      }
    };
  }

  public static InputStreamBufferer newFileInstance() {
    return new InputStreamBufferer() {
      @Override
      public BufferedInputStream buffer(InputStream in) throws IOException {
        BufferedInputStream result = null;

        File tmp = File.createTempFile("buffer.", ".bin");
        try {
          try (OutputStream out = new FileOutputStream(tmp)) {
            MoreByteStreams.drain(in, out);
          }
          result = new FileBufferedInputStream(tmp, true);
        } finally {
          if (result == null)
            tmp.delete();
        }

        return result;
      }
    };
  }

  public BufferedInputStream buffer(InputStream input) throws IOException;
}
