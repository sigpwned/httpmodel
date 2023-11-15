package com.sigpwned.httpmodel.core.model;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Optional;
import java.util.OptionalLong;
import com.sigpwned.httpmodel.core.io.ByteFilterSource;
import com.sigpwned.httpmodel.core.io.EntityInputStream;
import com.sigpwned.httpmodel.core.io.InputStreamBufferingStrategy;
import com.sigpwned.httpmodel.core.io.buffered.NullInputStream;
import com.sigpwned.httpmodel.core.util.MoreByteStreams;
import com.sigpwned.httpmodel.core.util.MoreCharStreams;

public abstract class ModelHttpEntityInputStream extends FilterInputStream {
  /**
   * Purely to make some methods visible to the outer class
   */
  private static class FriendlyEntityInputStream extends EntityInputStream {
    public FriendlyEntityInputStream(InputStream input) {
      super(input);
    }

    @Override
    public void filter(ByteFilterSource filterSource) throws IOException {
      super.filter(filterSource);
    }
  }

  private boolean hasEntity;

  public ModelHttpEntityInputStream(InputStream newInput) throws IOException {
    super(new FriendlyEntityInputStream(
        Optional.ofNullable(newInput).orElseGet(NullInputStream::new)));
    hasEntity = (newInput != null);
  }

  public boolean hasEntity() {
    return hasEntity;
  }

  public ModelHttpEntityInputStream replace(InputStream newInput) throws IOException {
    getDelegate().replace(Optional.ofNullable(newInput).orElseGet(NullInputStream::new));
    hasEntity = (newInput != null);
    return this;
  }

  protected void filter(ByteFilterSource filterSource) throws IOException {
    if (!hasEntity())
      throw new IllegalStateException("Cannot filter stream that represents no entity");
    getDelegate().filter(filterSource);
  }

  /**
   * @return {@code true} if this stream is buffered, and {@code false} otherwise.
   *
   * @see #restart()
   */
  public boolean isBuffered() {
    return getDelegate().isBuffered();
  }

  /**
   * If this stream is buffered, then re-starts the stream from the beginning.
   *
   * @throws IllegalStateException if the stream is not buffered
   * @throws IOException if there is a problem during I/O, in which case this stream is in an
   *         unknown state. Users should abort their current operation and close this stream.
   */
  public ModelHttpEntityInputStream restart() throws IOException {
    getDelegate().restart();
    return this;
  }

  public OptionalLong length() throws IOException {
    return getDelegate().length();
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
    return getDelegate().buffer(strategy);
  }

  public abstract Optional<ModelHttpMediaType> getContentType();

  public byte[] toByteArray() throws IOException {
    return MoreByteStreams.toByteArray(this);
  }

  public Reader readChars(Charset defaultCharset) throws IOException {
    return new InputStreamReader(this,
        getContentType().flatMap(ModelHttpMediaType::getCharset).orElse(defaultCharset));
  }

  public String toString(Charset defaultCharset) throws IOException {
    return MoreCharStreams.toString(readChars(defaultCharset));
  }

  protected ModelHttpEntityInputStream getDelegate() {
    return (ModelHttpEntityInputStream) in;
  }
}
