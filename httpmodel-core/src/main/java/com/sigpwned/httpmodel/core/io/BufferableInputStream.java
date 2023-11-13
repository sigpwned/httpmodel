package com.sigpwned.httpmodel.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.OptionalLong;

public class BufferableInputStream extends InputStream {
  private boolean start;
  private InputStream input;
  private boolean buffered;

  public BufferableInputStream(InputStream input) {
    if (input == null)
      throw new NullPointerException();
    this.input = input;
    this.start = true;
    this.buffered = input instanceof BufferedInputStream;
  }

  /**
   * @return {@code true} if this stream is buffered, and {@code false} otherwise.
   *
   * @see #restart()
   */
  public boolean isBuffered() {
    return buffered;
  }

  /**
   * If this stream is buffered, then re-starts the stream from the beginning.
   *
   * @throws IllegalStateException if the stream is not buffered
   * @throws IOException if there is a problem during I/O, in which case this stream is in an
   *         unknown state. Users should abort their current operation and close this stream.
   */
  public void restart() throws IOException {
    if (buffered == false)
      throw new IllegalStateException("not buffered");
    if (start == true)
      return;
    BufferedInputStream bufferedInput = (BufferedInputStream) input;
    bufferedInput.restart();
    start = true;
  }

  public OptionalLong length() throws IOException {
    if (buffered == false)
      return OptionalLong.empty();
    BufferedInputStream bufferedInput = (BufferedInputStream) input;
    return bufferedInput.length();
  }

  /**
   * Converts this stream into a buffered stream using the given
   * {@link InputStreamBufferingStrategy}. Must be called as the first operation on this stream.
   *
   * @param strategy The strategy to use to buffer this stream's contents
   * @return {@code true} if the given strategy was used to buffer this stream, or {@code false} if
   *         this stream was already buffered.
   * @throws IllegalStateException if this method was not called as the first operation on this
   *         stream
   * @throws IOException if there is a problem during I/O, in which case this stream is in an
   *         unknown state. Users should abort their current operation and close this stream.
   */
  public boolean buffer(InputStreamBufferingStrategy strategy) throws IOException {
    if (buffered == true)
      return false;
    if (start == false)
      throw new IllegalStateException("not at start of stream");

    InputStream unbufferedInput = input;
    BufferedInputStream bufferedInput = strategy.newBufferer().buffer(unbufferedInput);
    input = bufferedInput;
    unbufferedInput.close();
    buffered = true;
    start = true;

    return true;
  }

  @Override
  public int read() throws IOException {
    start = false;
    return input.read();
  }

  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    start = false;
    return input.read(b, off, len);
  }

  @Override
  public int read(byte[] b) throws IOException {
    start = false;
    return input.read(b);
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
  public synchronized void reset() throws IOException {
    start = false;
    input.reset();
  }

  @Override
  public long skip(long n) throws IOException {
    start = false;
    return input.skip(n);
  }

  @Override
  public void close() throws IOException {
    input.close();
  }
}
