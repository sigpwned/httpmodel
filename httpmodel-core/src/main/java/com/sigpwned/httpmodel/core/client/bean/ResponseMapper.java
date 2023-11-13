package com.sigpwned.httpmodel.core.client.bean;

import java.io.IOException;
import com.sigpwned.httpmodel.core.model.ModelHttpMediaType;
import com.sigpwned.httpmodel.core.model.ModelHttpRequest;
import com.sigpwned.httpmodel.core.model.ModelHttpResponse;

public interface ResponseMapper<T> {
  public boolean isMappable(Class<?> responseType, ModelHttpMediaType contentType);

  public T mapResponse(ModelHttpRequest request, ModelHttpResponse response) throws IOException;
}
