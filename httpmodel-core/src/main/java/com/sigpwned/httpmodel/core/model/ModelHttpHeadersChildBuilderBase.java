package com.sigpwned.httpmodel.core.model;

import static java.util.Collections.emptyList;
import java.util.List;
import com.sigpwned.httpmodel.core.model.ModelHttpHeaders.Header;

public class ModelHttpHeadersChildBuilderBase<ParentT, BuilderT extends ModelHttpHeadersChildBuilderBase<ParentT, BuilderT>>
    extends ModelHttpHeadersBuilderBase<BuilderT> {
  private final ParentT parent;

  public ModelHttpHeadersChildBuilderBase(ParentT parent) {
    this(parent, emptyList());
  }

  public ModelHttpHeadersChildBuilderBase(ParentT parent, BuilderT that) {
    this(parent, that.headers());
  }

  public ModelHttpHeadersChildBuilderBase(ParentT parent, ModelHttpHeaders that) {
    this(parent, that.getHeaders());
  }

  public ModelHttpHeadersChildBuilderBase(ParentT parent, List<Header> headers) {
    super(headers);
    if (parent == null)
      throw new NullPointerException();
    this.parent = parent;
  }

  public ParentT done() {
    return parent;
  }
}
