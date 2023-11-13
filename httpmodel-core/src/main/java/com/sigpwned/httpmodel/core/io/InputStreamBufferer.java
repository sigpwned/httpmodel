package com.sigpwned.httpmodel.core.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.sigpwned.httpmodel.core.io.buffered.FileBufferedInputStream;
import com.sigpwned.httpmodel.core.io.buffered.MemoryBufferedInputStream;
import com.sigpwned.httpmodel.core.io.buffered.NullBufferedInputStream;
import com.sigpwned.httpmodel.core.util.MoreByteStreams;

@FunctionalInterface
public interface InputStreamBufferer {
  public static final int DEFAULT_MAX_BUFFER_SIZE = 64 * 1024;

  /**
   * If the given {@link InputStream} contains fewer than {@link #DEFAULT_MAX_BUFFER_SIZE} bytes,
   * then returns a {@link MemoryBufferedInputStream}. Otherwise, writes the contents of the stream
   * to a temporary file and returns a {@link FileBufferedInputStream}.
   *
   * @see File#createTempFile(String, String)
   */
  public static InputStreamBufferer newDefaultInstance() {
    return new InputStreamBufferer() {
      @Override
      public BufferedInputStream buffer(InputStream in) throws IOException {
        byte[] buf = new byte[DEFAULT_MAX_BUFFER_SIZE];
        int nread = MoreByteStreams.read(in, buf);
        if (nread == 0)
          return new NullBufferedInputStream();
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

  public BufferedInputStream buffer(InputStream input) throws IOException;
}
