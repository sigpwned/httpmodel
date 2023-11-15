package com.sigpwned.httpmodel.core.client;

import java.io.IOException;
import com.sigpwned.httpmodel.core.model.ModelHttpRequest;
import com.sigpwned.httpmodel.core.model.ModelHttpResponse;

public interface ModelHttpConnector extends AutoCloseable {
  public ModelHttpResponse send(ModelHttpRequest request) throws IOException;

  @Override
  default void close() throws IOException {}
}
