package com.sigpwned.httpmodel.core;

import java.io.IOException;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;

/**
 * Customize an HTTP request version, method, URL, or headers. Cannot change request body.
 */
public interface ModelHttpRequestFilter {
  public static final int DEFAULT_PRIORITY = 1000000;

  /**
   * Filters are run in order of increasing priority
   */
  default int priority() {
    return DEFAULT_PRIORITY;
  }

  public void filter(ModelHttpRequestHead requestHead) throws IOException;
}
