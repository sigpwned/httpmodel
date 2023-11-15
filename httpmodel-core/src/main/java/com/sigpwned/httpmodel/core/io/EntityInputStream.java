package com.sigpwned.httpmodel.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;
import java.util.zip.GZIPInputStream;

/**
 * <p>
 * An {@link InputStream} that supports <em>replacement</em>, <em>buffering</em> and
 * <em>filtering</em>.
 * </p>
 *
 * <p>
 * <em>Replacement</em> means completely replacing the underlying input stream with a new one. This
 * is useful when rewriting HTTP entities.
 * </p>
 *
 * <p>
 * <em>Buffering</em> means creating a copy of the contents of the stream for possible replay later.
 * Note that these semantics are different from {@link java.io.BufferedInputStream}. This is useful
 * when analyzing request contents, e.g., when signing requests.
 * </p>
 *
 * <p>
 * <em>Filtering</em> means wrapping the underlying input stream in another input stream for
 * transformation, e.g., a {@link GZIPInputStream} for inline decompression.
 * </p>
 */
public class EntityInputStream extends InputStream {
  /**
   * If {@code true}, then no bytes have been read since this stream was opened or restarted.
   *
   * @see #restart()
   */
  private boolean atStart;

  /**
   * The ordered list of {@link ByteFilterSource} filters applied to this stream.
   */
  private final List<ByteFilterSource> filterSources;

  /**
   * The "root" input stream from which this stream's input ultimately originates. If we visualize
   * this stream's byte pipeline as a linked list, then this stream is that list's head.
   */
  private InputStream headInputStream;

  /**
   * The "leaf" input stream from which this stream's input directly originates. If we visualize
   * this stream's byte pipeline as a linked list, then this stream is that stream's tail.
   */
  private InputStream tailInputStream;

  /**
   * If {@code true}, then this stream's {@link #close()} method has been called. Otherwise, it has
   * not been called.
   */
  private boolean closed;

  public EntityInputStream(InputStream input) {
    if (input == null)
      throw new NullPointerException();
    this.filterSources = new ArrayList<>(4);
    this.closed = false;
    try {
      replace(input);
    } catch (IOException e) {
      // This should never happen, since we're not closed and there's nothing to close.
      throw new AssertionError("Failed to initialize BufferableInputStream", e);
    }
  }

  /**
   *
   * @param newInputStream
   * @throws IOException
   */
  public void replace(InputStream newInputStream) throws IOException {
    if (isClosed())
      throw new IOException("closed");
    filterSources.clear();
    if (tailInputStream != null)
      tailInputStream.close();
    headInputStream = this.tailInputStream = newInputStream;
    atStart = true;
  }

  protected void filter(ByteFilterSource filterSource) throws IOException {
    if (isClosed())
      throw new IOException("closed");
    if (!isAtStart())
      throw new IllegalStateException("not at start");
    filterSources.add(filterSource);
    InputStream newInputStream = filterSource.filter(tailInputStream);
    tailInputStream = newInputStream;
  }

  /**
   * @return {@code true} if this stream is buffered, and {@code false} otherwise.
   *
   * @see #restart()
   */
  public boolean isBuffered() {
    return headInputStream instanceof BufferedInputStream;
  }

  /**
   * If this stream is buffered, then re-starts the stream from the beginning.
   *
   * @throws IllegalStateException if the stream is not buffered
   * @throws IOException if there is a problem during I/O, in which case this stream is in an
   *         unknown state. Users should abort their current operation and close this stream.
   */
  public void restart() throws IOException {
    if (isClosed())
      throw new IOException("closed");
    if (!isBuffered())
      throw new IllegalStateException("not buffered");
    if (isAtStart())
      return;

    List<ByteFilterSource> filterSources = new ArrayList<>(this.filterSources);
    BufferedInputStream bufferedInputStream = (BufferedInputStream) headInputStream;

    replace(bufferedInputStream);

    bufferedInputStream.restart();
    for (ByteFilterSource filterSource : filterSources)
      filter(filterSource);
  }

  public OptionalLong length() throws IOException {
    if (isClosed())
      throw new IOException("closed");
    if (isFiltered())
      return OptionalLong.empty();
    if (!isBuffered())
      return OptionalLong.empty();
    BufferedInputStream bufferedInputStream = (BufferedInputStream) headInputStream;
    return bufferedInputStream.length();
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
    if (isClosed())
      throw new IOException("closed");
    if (!isAtStart())
      throw new IllegalStateException("not at start of stream");
    if (isFiltered())
      throw new IllegalStateException("stream cannot be buffered after filtering");

    // If we're already buffered, then return false, since this call did not buffer this stream
    if (isBuffered())
      return false;

    // Use this strategy to buffer this stream
    boolean successful = false;
    InputStream bufferedInput = strategy.newBufferer().buffer(headInputStream);
    try {
      replace(bufferedInput);
      successful = true;
    } finally {
      if (!successful)
        bufferedInput.close();
    }

    return true;
  }

  @Override
  public int read() throws IOException {
    if (isClosed())
      throw new IOException("closed");
    atStart = false;
    return tailInputStream.read();
  }

  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    if (isClosed())
      throw new IOException("closed");
    atStart = false;
    return tailInputStream.read(b, off, len);
  }

  @Override
  public int read(byte[] b) throws IOException {
    if (isClosed())
      throw new IOException("closed");
    atStart = false;
    return tailInputStream.read(b);
  }

  @Override
  public int available() throws IOException {
    if (isClosed())
      throw new IOException("closed");
    return tailInputStream.available();
  }

  @Override
  public synchronized void mark(int readlimit) {
    tailInputStream.mark(readlimit);
  }

  @Override
  public boolean markSupported() {
    return tailInputStream.markSupported();
  }

  @Override
  public synchronized void reset() throws IOException {
    if (isClosed())
      throw new IOException("closed");
    atStart = false;
    tailInputStream.reset();
  }

  @Override
  public long skip(long n) throws IOException {
    if (isClosed())
      throw new IOException("closed");
    atStart = false;
    return tailInputStream.skip(n);
  }

  @Override
  public void close() throws IOException {
    if (isClosed())
      throw new IOException("closed");
    tailInputStream.close();
  }

  protected boolean isAtStart() {
    return atStart;
  }

  protected boolean isClosed() {
    return closed;
  }

  protected boolean isFiltered() {
    return tailInputStream != headInputStream;
  }
}
