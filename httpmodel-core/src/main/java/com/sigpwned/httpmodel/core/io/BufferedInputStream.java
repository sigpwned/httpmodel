package com.sigpwned.httpmodel.core.io;

import java.io.FileNotFoundException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.OptionalLong;

public abstract class BufferedInputStream extends FilterInputStream {
  public BufferedInputStream() {
    super(null);
  }

  /**
   * Re-starts this stream from its beginning. If the stream was previously closed, then it can now
   * be read until it is closed again.
   *
   * @see #close()
   */
  public void restart() throws IOException {
    InputStream newInputStream = newInputStream();
    if (newInputStream == null)
      throw new NullPointerException();
    in.close();
    in = newInputStream;
  }

  /**
   * total bytes, not remaining
   */
  public abstract OptionalLong length() throws IOException;

  /**
   * hook
   *
   * @param file
   * @return An {@link InputStream} containing the same bytes as the original argument to the
   *         constructor. Must not be {@code null}.
   * @throws FileNotFoundException
   */
  protected abstract InputStream newInputStream() throws IOException;
}
