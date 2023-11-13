package com.sigpwned.httpmodel.core.entity;

import java.io.InputStream;
import java.util.Optional;
import com.sigpwned.httpmodel.core.io.BufferableInputStream;
import com.sigpwned.httpmodel.core.model.ModelHttpMediaType;

public abstract class ModelHttpEntityInputStream extends BufferableInputStream {
  public ModelHttpEntityInputStream(InputStream input) {
    super(input);
  }

  public abstract Optional<ModelHttpMediaType> getContentType();
}
