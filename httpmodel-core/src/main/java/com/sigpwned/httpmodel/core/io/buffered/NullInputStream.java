package com.sigpwned.httpmodel.core.io.buffered;

import java.io.IOException;
import java.io.InputStream;
import java.util.OptionalLong;
import com.sigpwned.httpmodel.core.io.BufferedInputStream;

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
    return new NullInputStream();
  }
}
