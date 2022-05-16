package com.sigpwned.httpmodel.util;

import com.sigpwned.httpmodel.ModelHttpMediaType;

public final class ModelHttpMediaTypes {
  private ModelHttpMediaTypes() {}

  public static final ModelHttpMediaType APPLICATION_OCTET_STREAM =
      ModelHttpMediaType.of("application", "octet-stream");

  public static final ModelHttpMediaType APPLICATION_X_WWW_FORM_URLENCODED =
      ModelHttpMediaType.of("application", "x-www-form-urlencoded");
}
