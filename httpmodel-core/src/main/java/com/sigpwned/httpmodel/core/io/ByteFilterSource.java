package com.sigpwned.httpmodel.core.io;

import java.io.IOException;
import java.io.InputStream;

@FunctionalInterface
public interface ByteFilterSource {
  /**
   * @param in The input stream to filter
   * @return A new {@link InputStream} that filters the contents of the given input stream. Closing
   *         the new input stream must close the given input stream.
   * @throws IOException
   */
  public InputStream filter(InputStream in) throws IOException;
}
