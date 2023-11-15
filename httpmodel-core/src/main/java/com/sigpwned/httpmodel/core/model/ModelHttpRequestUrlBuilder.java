package com.sigpwned.httpmodel.core.model;

public class ModelHttpRequestUrlBuilder extends
    ModelHttpUrlChildBuilderBase<ModelHttpRequestBuilder, ModelHttpRequestUrlQueryStringBuilder, ModelHttpRequestUrlBuilder> {
  public ModelHttpRequestUrlBuilder(ModelHttpRequestBuilder parent) {
    super(parent);
  }

  public ModelHttpRequestUrlBuilder(ModelHttpRequestBuilder parent,
      ModelHttpRequestUrlBuilder that) {
    super(parent, that.scheme(), that.authority(), that.path(), that.queryString());
  }

  @Override
  protected ModelHttpRequestUrlQueryStringBuilder newQueryStringBuilder() {
    return new ModelHttpRequestUrlQueryStringBuilder(this);
  }
}
