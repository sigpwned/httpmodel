package com.sigpwned.httpmodel.core.client.bean;

import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;
import com.sigpwned.httpmodel.core.model.ModelHttpResponseHead;

public interface ModelHttpBeanClientResponseFilter {
  public static final int DEFAULT_PRIORITY = 1000000;

  /**
   * Filters are run in order of increasing priority
   */
  default int priority() {
    return DEFAULT_PRIORITY;
  }

  public void filter(ModelHttpRequestHead httpRequestHead, Object requestBean,
      ModelHttpResponseHead httpResponseHead, Object responseBean);
}