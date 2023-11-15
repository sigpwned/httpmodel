package com.sigpwned.httpmodel.core.client;

import java.io.IOException;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;

public interface ModelHttpRequestFilter {
  public static final int DEFAULT_PRIORITY = 1000000;

  default int priority() {
    return DEFAULT_PRIORITY;
  }

  public void filter(ModelHttpRequestHead requestHead) throws IOException;
}
