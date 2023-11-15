package com.sigpwned.httpmodel.core.model;

public abstract class ModelHttpUrlChildBuilderBase<ParentT, QueryStringBuilderT extends ModelHttpQueryStringBuilderBase<QueryStringBuilderT>, BuilderT extends ModelHttpUrlBuilderBase<QueryStringBuilderT, BuilderT>>
    extends ModelHttpUrlBuilderBase<QueryStringBuilderT, BuilderT> {
  private final ParentT parent;

  public ModelHttpUrlChildBuilderBase(ParentT parent) {
    this(parent, null, null, null, null);
  }

  public ModelHttpUrlChildBuilderBase(ParentT parent, BuilderT that) {
    this(parent, that.scheme(), that.authority(), that.path(), that.queryString());
  }

  public ModelHttpUrlChildBuilderBase(ParentT parent, String scheme, ModelHttpAuthority authority,
      String path, QueryStringBuilderT queryString) {
    super(scheme, authority, path, queryString);
    if (parent == null)
      throw new NullPointerException();
    this.parent = parent;
  }

  public ParentT done() {
    return parent;
  }
}
