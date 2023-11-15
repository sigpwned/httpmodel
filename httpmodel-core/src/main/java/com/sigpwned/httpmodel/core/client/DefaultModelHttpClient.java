package com.sigpwned.httpmodel.core.client;

import java.io.IOException;
import com.sigpwned.httpmodel.core.ModelHttpClient;
import com.sigpwned.httpmodel.core.model.ModelHttpRequest;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;
import com.sigpwned.httpmodel.core.model.ModelHttpResponse;
import com.sigpwned.httpmodel.core.model.ModelHttpResponseHead;

public class DefaultModelHttpClient extends DefaultModelHttpClientBase implements ModelHttpClient {
  public DefaultModelHttpClient(ModelHttpConnector connector) {
    super(connector);
  }

  @Override
  public ModelHttpResponse send(ModelHttpRequest request) throws IOException {
    doRequestFilters(ModelHttpRequestHead.fromRequest(request));

    doRequestInterceptors(request);

    ModelHttpResponse response = doSend(request);

    doResponseFilters(ModelHttpResponseHead.fromResponse(response));

    doResponseInterceptors(response);

    return response;
  }

  /**
   * hook
   *
   * @param request
   * @return
   * @throws IOException
   */
  protected ModelHttpResponse doSend(ModelHttpRequest request) throws IOException {
    return getConnector().send(request);
  }
}
