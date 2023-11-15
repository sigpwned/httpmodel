package com.sigpwned.httpmodel.core;

import java.io.IOException;
import com.sigpwned.httpmodel.core.model.ModelHttpResponse;

/**
 * Customize an HTTP response status code, headers, or body.
 */
public interface ModelHttpResponseInterceptor {
  public static final int DEFAULT_PRIORITY = 1000000;

  /**
   * Interceptors are run in order of increasing priority
   */
  default int priority() {
    return DEFAULT_PRIORITY;
  }

  public void intercept(ModelHttpResponse response) throws IOException;
}
