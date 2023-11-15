package com.sigpwned.httpmodel.core.client;

import java.io.IOException;
import com.sigpwned.httpmodel.core.model.ModelHttpRequest;

public interface ModelHttpRequestInterceptor {
  public static final int DEFAULT_PRIORITY = 1000000;

  default int priority() {
    return DEFAULT_PRIORITY;
  }

  public void intercept(ModelHttpRequest request) throws IOException;
}
