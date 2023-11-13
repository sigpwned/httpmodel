package com.sigpwned.httpmodel.core.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public abstract class BufferedInputStream extends InputStream {
  private InputStream input;

  public BufferedInputStream(InputStream input) {
    if (input == null)
      throw new NullPointerException();
    this.input = input;
  }

  public void restart() throws IOException {
    input.close();
    input = newInputStream();
  }

  @Override
  public int available() throws IOException {
    return input.available();
  }

  @Override
  public synchronized void mark(int readlimit) {
    input.mark(readlimit);
  }

  @Override
  public boolean markSupported() {
    return input.markSupported();
  }

  @Override
  public int read() throws IOException {
    return input.read();
  }

  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    return input.read(b, off, len);
  }

  @Override
  public int read(byte[] b) throws IOException {
    return input.read(b);
  }

  @Override
  public synchronized void reset() throws IOException {
    input.reset();
  }

  @Override
  public long skip(long n) throws IOException {
    return input.skip(n);
  }

  @Override
  public void close() throws IOException {
    input.close();
  }

  /**
   * hook
   *
   * @param file
   * @return
   * @throws FileNotFoundException
   */
  protected abstract InputStream newInputStream() throws IOException;
}
