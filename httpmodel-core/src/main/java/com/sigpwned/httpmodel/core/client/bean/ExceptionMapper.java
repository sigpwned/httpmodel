package com.sigpwned.httpmodel.core.client.bean;

import java.io.IOException;
import com.sigpwned.httpmodel.core.model.ModelHttpResponse;

public interface ExceptionMapper {
  public IOException mapException(ModelHttpResponse response);
}
