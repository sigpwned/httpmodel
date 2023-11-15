package com.sigpwned.httpmodel.core.client.bean;

import java.io.IOException;
import com.sigpwned.httpmodel.core.model.ModelHttpMediaType;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;
import com.sigpwned.httpmodel.core.model.ModelHttpResponse;

public interface ModelHttpClientResponseMapper<T> {
  public boolean isMappable(Class<?> responseType, ModelHttpMediaType contentType);

  public T mapResponse(ModelHttpRequestHead httpRequestHead, ModelHttpResponse httpResponse)
      throws IOException;
}
