package com.sigpwned.httpmodel.core.io;

import java.io.IOException;
import java.io.InputStream;

public class NullInputStream extends InputStream {
  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    return -1;
  }

  @Override
  public int read(byte[] b) throws IOException {
    return -1;
  }

  @Override
  public int read() throws IOException {
    return -1;
  }
}
