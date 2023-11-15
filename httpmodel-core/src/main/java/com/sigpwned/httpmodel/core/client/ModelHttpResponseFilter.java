package com.sigpwned.httpmodel.core.client;

import com.sigpwned.httpmodel.core.model.ModelHttpResponseHead;

public interface ModelHttpResponseFilter {
  public void filter(ModelHttpResponseHead responseHead);
}
