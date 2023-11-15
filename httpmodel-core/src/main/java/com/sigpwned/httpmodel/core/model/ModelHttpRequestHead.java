package com.sigpwned.httpmodel.core.model;

import com.sigpwned.httpmodel.core.util.ModelHttpMethods;
import com.sigpwned.httpmodel.core.util.ModelHttpVersions;

public class ModelHttpRequestHead {
  public static ModelHttpRequestHeadBuilder builder() {
    return new ModelHttpRequestHeadBuilder();
  }

  public static ModelHttpRequestHead fromRequest(ModelHttpRequest request) {
    return new ModelHttpRequestHead(request.getVersion(), request.getMethod(), request.getUrl(),
        request.getHeaders());
  }

  /**
   * @see ModelHttpVersions
   */
  private String version;

  /**
   * @see ModelHttpMethods
   */
  private String method;

  private ModelHttpUrl url;

  private ModelHttpHeaders headers;

  public ModelHttpRequestHead(String version, String method, ModelHttpUrl url,
      ModelHttpHeaders headers) {
    if (version == null)
      throw new NullPointerException();
    if (method == null)
      throw new NullPointerException();
    if (url == null)
      throw new NullPointerException();
    if (headers == null)
      throw new NullPointerException();
  }

  /* default */ ModelHttpRequestHead(ModelHttpRequestHeadBuilder b) {
    this(b.version(), b.method(), b.url().build(), b.headers().build());
  }

  public String getVersion() {
    return version;
  }

  public ModelHttpRequestHead setVersion(String version) {
    this.version = version;
    return this;
  }

  public String getMethod() {
    return method;
  }

  public ModelHttpRequestHead setMethod(String method) {
    this.method = method;
    return this;
  }

  public ModelHttpUrl getUrl() {
    return url;
  }

  public ModelHttpRequestHead setUrl(ModelHttpUrl url) {
    this.url = url;
    return this;
  }

  public ModelHttpHeaders getHeaders() {
    return headers;
  }

  public ModelHttpRequestHead setHeaders(ModelHttpHeaders headers) {
    this.headers = headers;
    return this;
  }

  public ModelHttpRequestHeadBuilder toBuilder() {
    return new ModelHttpRequestHeadBuilder(this);
  }

  @Override
  public String toString() {
    return "ModelHttpRequestHead [version=" + version + ", method=" + method + ", url=" + url
        + ", headers=" + headers + "]";
  }
}
