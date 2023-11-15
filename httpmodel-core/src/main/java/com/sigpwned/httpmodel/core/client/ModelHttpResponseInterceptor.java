package com.sigpwned.httpmodel.core.client;

import com.sigpwned.httpmodel.core.model.ModelHttpResponse;

public interface ModelHttpResponseInterceptor {
  public void intercept(ModelHttpResponse response);
}
