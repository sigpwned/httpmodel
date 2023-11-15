package com.sigpwned.httpmodel.core;

import java.io.IOException;
import com.sigpwned.httpmodel.core.model.ModelHttpResponseHead;

/**
 * Customize an HTTP response status code or headers. Cannot touch response body.
 */
public interface ModelHttpResponseFilter {
  public static final int DEFAULT_PRIORITY = 1000000;

  /**
   * Filters are run in order of increasing priority
   */
  default int priority() {
    return DEFAULT_PRIORITY;
  }

  public void filter(ModelHttpResponseHead responseHead) throws IOException;
}
