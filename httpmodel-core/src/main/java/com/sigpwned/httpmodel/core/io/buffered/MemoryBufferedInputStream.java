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
