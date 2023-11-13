package com.sigpwned.httpmodel.core.io.buffered;

import java.io.IOException;
import java.io.InputStream;
import java.util.OptionalLong;
import com.sigpwned.httpmodel.core.io.BufferedInputStream;
import com.sigpwned.httpmodel.core.io.NullInputStream;

public class NullBufferedInputStream extends BufferedInputStream {
  public NullBufferedInputStream() {
    in = newInputStream();
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
