package com.sigpwned.httpmodel.core.client.bean;

import java.io.IOException;
import com.sigpwned.httpmodel.core.model.ModelHttpMediaType;
import com.sigpwned.httpmodel.core.model.ModelHttpRequest;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;

public interface ModelHttpBeanClientRequestMapper<T> {
  public boolean isMappable(Class<?> requestType, ModelHttpMediaType contentType);

  public ModelHttpRequest mapRequest(ModelHttpRequestHead requestHead, T value) throws IOException;
}
