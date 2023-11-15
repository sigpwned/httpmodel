package com.sigpwned.httpmodel.core.client.bean;

import java.io.IOException;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;
import com.sigpwned.httpmodel.core.model.ModelHttpResponse;

public interface ModelHttpClientExceptionMapper {
  public IOException mapException(ModelHttpRequestHead httpRequestHead,
      ModelHttpResponse httpResponseHead);
}
