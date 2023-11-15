package com.sigpwned.httpmodel.core.model;

import static java.util.Collections.emptyList;
import java.util.List;
import com.sigpwned.httpmodel.core.model.ModelHttpQueryString.Parameter;

public class ModelHttpQueryStringChildBuilderBase<ParentT, BuilderT extends ModelHttpQueryStringChildBuilderBase<ParentT, BuilderT>>
    extends ModelHttpQueryStringBuilderBase<BuilderT> {
  private final ParentT parent;

  public ModelHttpQueryStringChildBuilderBase(ParentT parent) {
    this(parent, emptyList());
  }

  public ModelHttpQueryStringChildBuilderBase(ParentT parent, BuilderT that) {
    this(parent, that.parameters());
  }

  public ModelHttpQueryStringChildBuilderBase(ParentT parent, ModelHttpQueryString that) {
    this(parent, that.getParameters());
  }

  public ModelHttpQueryStringChildBuilderBase(ParentT parent, List<Parameter> parameters) {
    super(parameters);
    if (parent == null)
      throw new NullPointerException();
    this.parent = parent;
  }

  public ParentT done() {
    return parent;
  }
}
