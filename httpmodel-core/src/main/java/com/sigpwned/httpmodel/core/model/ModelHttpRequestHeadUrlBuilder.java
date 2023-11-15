package com.sigpwned.httpmodel.core.model;

public class ModelHttpRequestHeadUrlBuilder extends
    ModelHttpUrlChildBuilderBase<ModelHttpRequestHeadBuilder, ModelHttpRequestHeadUrlQueryStringBuilder, ModelHttpRequestHeadUrlBuilder> {
  public ModelHttpRequestHeadUrlBuilder(ModelHttpRequestHeadBuilder parent) {
    super(parent);
  }

  public ModelHttpRequestHeadUrlBuilder(ModelHttpRequestHeadBuilder parent,
      ModelHttpRequestHeadUrlBuilder that) {
    super(parent, that.scheme(), that.authority(), that.path(), that.queryString());
  }

  @Override
  protected ModelHttpRequestHeadUrlQueryStringBuilder newQueryStringBuilder() {
    return new ModelHttpRequestHeadUrlQueryStringBuilder(this);
  }
}
