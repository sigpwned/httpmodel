package com.sigpwned.httpmodel.core.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Optional;
import com.sigpwned.httpmodel.core.io.BufferableInputStream;
import com.sigpwned.httpmodel.core.io.NullInputStream;
import com.sigpwned.httpmodel.core.util.MoreByteStreams;
import com.sigpwned.httpmodel.core.util.MoreCharStreams;

public abstract class ModelHttpEntityInputStream extends BufferableInputStream {
  private final boolean hasEntity;

  public ModelHttpEntityInputStream(InputStream input) {
    super(Optional.ofNullable(input).orElseGet(NullInputStream::new));
    this.hasEntity = (input != null);
  }

  public boolean hasEntity() {
    return hasEntity;
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
