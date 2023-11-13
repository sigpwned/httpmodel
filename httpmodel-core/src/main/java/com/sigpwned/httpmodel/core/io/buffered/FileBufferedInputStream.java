package com.sigpwned.httpmodel.core.io.buffered;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.OptionalLong;
import com.sigpwned.httpmodel.core.io.BufferedInputStream;

public class FileBufferedInputStream extends BufferedInputStream {
  private final File file;
  private final boolean deleteOnClose;

  public FileBufferedInputStream(File file, boolean deleteOnClose) throws IOException {
    this.file = file;
    this.deleteOnClose = deleteOnClose;
    in = newInputStream();
  }

  @Override
  public OptionalLong length() throws IOException {
    return OptionalLong.of(file.length());
  }

  @Override
  public void close() throws IOException {
    try {
      super.close();
    } finally {
      if (deleteOnClose)
        file.delete();
    }
  }

  /**
   * hook
   */
  @Override
  protected InputStream newInputStream() throws IOException {
    return new FileInputStream(file);
  }
}
