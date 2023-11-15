package com.sigpwned.httpmodel.core.model;

import com.sigpwned.httpmodel.core.util.ModelHttpStatusCodes;

public class ModelHttpResponseHead {
  public static ModelHttpResponseHead fromResponse(ModelHttpResponse response) {
    return new ModelHttpResponseHead(response.getStatusCode(), response.getHeaders());
  }

  /**
   * @see ModelHttpStatusCodes
   */
  private int statusCode;

  private ModelHttpHeaders headers;

  public ModelHttpResponseHead(int statusCode, ModelHttpHeaders headers) {
    if (headers == null)
      throw new NullPointerException();
    this.statusCode = statusCode;
    this.headers = headers;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public ModelHttpResponseHead setStatusCode(int statusCode) {
    this.statusCode = statusCode;
    return this;
  }

  public ModelHttpHeaders getHeaders() {
    return headers;
  }

  public ModelHttpResponseHead setHeaders(ModelHttpHeaders headers) {
    this.headers = headers;
    return this;
  }

  @Override
  public String toString() {
    return "ModelHttpResponseHead [statusCode=" + statusCode + ", headers=" + headers + "]";
  }

}
