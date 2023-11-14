package com.sigpwned.httpmodel.core.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Optional;
import com.sigpwned.httpmodel.core.io.ByteFilterSource;
import com.sigpwned.httpmodel.core.io.EntityInputStream;
import com.sigpwned.httpmodel.core.io.buffered.NullInputStream;
import com.sigpwned.httpmodel.core.util.MoreByteStreams;
import com.sigpwned.httpmodel.core.util.MoreCharStreams;

public abstract class ModelHttpEntityInputStream extends EntityInputStream {
  private boolean hasEntity;

  public ModelHttpEntityInputStream(InputStream input) throws IOException {
    super(Optional.ofNullable(input).orElseGet(NullInputStream::new));
    hasEntity = (input != null);
  }

  public boolean hasEntity() {
    return hasEntity;
  }

  @Override
  public void replace(InputStream newInput) throws IOException {
    super.replace(Optional.ofNullable(newInput).orElseGet(NullInputStream::new));
    hasEntity = (newInput != null);
  }

  @Override
  protected void filter(ByteFilterSource filterSource) throws IOException {
    if (!hasEntity())
      throw new IllegalStateException("Cannot filter stream that represents no entity");
    super.filter(filterSource);
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
}
