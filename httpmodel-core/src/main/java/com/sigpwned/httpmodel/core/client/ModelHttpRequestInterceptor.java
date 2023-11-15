package com.sigpwned.httpmodel.core.client;

import com.sigpwned.httpmodel.core.model.ModelHttpRequest;

public interface ModelHttpRequestInterceptor {
  public void intercept(ModelHttpRequest request);
}
