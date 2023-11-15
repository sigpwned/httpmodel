package com.sigpwned.httpmodel.core.client;

import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;

public interface ModelHttpRequestFilter {
  public void filter(ModelHttpRequestHead requestHead);
}
