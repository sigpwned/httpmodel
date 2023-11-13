package com.sigpwned.httpmodel.core.io.buffered;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import com.sigpwned.httpmodel.core.io.BufferedInputStream;

public class FileBufferedInputStream extends BufferedInputStream {
  private final File file;
  private final boolean deleteOnClose;

  public FileBufferedInputStream(File file, boolean deleteOnClose) throws IOException {
    super(new FileInputStream(file));
    this.file = file;
    this.deleteOnClose = deleteOnClose;
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
