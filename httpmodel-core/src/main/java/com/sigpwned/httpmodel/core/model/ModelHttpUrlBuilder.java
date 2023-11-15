package com.sigpwned.httpmodel.core.model;

public class ModelHttpUrlBuilder
    extends ModelHttpUrlBuilderBase<ModelHttpQueryStringBuilder, ModelHttpUrlBuilder> {
  public ModelHttpUrlBuilder() {}

  public ModelHttpUrlBuilder(ModelHttpUrl that) {
    this(that.getScheme(), that.getAuthority(), that.getPath(), that.getQueryString().toBuilder());
  }

  public ModelHttpUrlBuilder(ModelHttpUrlBuilder that) {
    super(that);
  }

  public ModelHttpUrlBuilder(String scheme, ModelHttpAuthority authority, String path,
      ModelHttpQueryStringBuilder queryString) {
    super(scheme, authority, path, queryString);
  }

  @Override
  public ModelHttpUrl build() {
    return super.build();
  }

  @Override
  protected ModelHttpQueryStringBuilder newQueryStringBuilder() {
    return new ModelHttpQueryStringBuilder();
  }
}
