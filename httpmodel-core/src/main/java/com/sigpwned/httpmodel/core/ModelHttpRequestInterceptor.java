package com.sigpwned.httpmodel.core;

import java.io.IOException;
import com.sigpwned.httpmodel.core.model.ModelHttpRequest;

/**
 * Customize an HTTP request version, method, URL, headers, or body.
 */
public interface ModelHttpRequestInterceptor {
  public static final int DEFAULT_PRIORITY = 1000000;

  /**
   * Interceptors are run in order of increasing priority
   */
  default int priority() {
    return DEFAULT_PRIORITY;
  }

  public void intercept(ModelHttpRequest request) throws IOException;
}
