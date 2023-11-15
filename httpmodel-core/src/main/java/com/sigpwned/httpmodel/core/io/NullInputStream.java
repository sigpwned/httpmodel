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
