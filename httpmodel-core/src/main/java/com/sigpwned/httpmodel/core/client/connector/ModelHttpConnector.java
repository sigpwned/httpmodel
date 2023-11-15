package com.sigpwned.httpmodel.core.client.connector;

import java.io.IOException;
import com.sigpwned.httpmodel.core.client.ModelHttpClient;
import com.sigpwned.httpmodel.core.client.bean.ModelHttpBeanClient;
import com.sigpwned.httpmodel.core.model.ModelHttpRequest;
import com.sigpwned.httpmodel.core.model.ModelHttpResponse;

/**
 * <p>
 * A pluggable HTTP implementation used for transport.
 * </p>
 *
 * @see ModelHttpClient
 * @see ModelHttpBeanClient
 */
public interface ModelHttpConnector extends AutoCloseable {
  public ModelHttpResponse send(ModelHttpRequest request) throws IOException;

  @Override
  default void close() throws IOException {}
}
