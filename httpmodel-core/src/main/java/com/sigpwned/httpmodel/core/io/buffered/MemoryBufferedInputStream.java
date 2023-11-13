package com.sigpwned.httpmodel.core.io.buffered;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import com.sigpwned.httpmodel.core.io.BufferedInputStream;

public class MemoryBufferedInputStream extends BufferedInputStream {
  private final byte[] bytes;
  private final int off;
  private final int len;

  public MemoryBufferedInputStream(byte[] bytes) throws IOException {
    this(bytes, 0, bytes.length);
  }

  public MemoryBufferedInputStream(byte[] bytes, int off, int len) throws IOException {
    super(new ByteArrayInputStream(bytes, off, len));
    this.bytes = bytes;
    this.off = off;
    this.len = len;
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
  protected InputStream newInputStream() throws IOException {
    return new ByteArrayInputStream(bytes, off, len);
  }
}
