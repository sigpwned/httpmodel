package com.sigpwned.httpmodel.core.client.bean;

import java.io.IOException;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestBuilder;

public interface RequestMapper<T> {
  public boolean isMappable(Class<?> type);

  public void mapRequest(ModelHttpRequestBuilder requestBuilder, T value) throws IOException;
}
