package com.sigpwned.httpmodel.core.model;

import java.util.List;
import com.sigpwned.httpmodel.core.model.ModelHttpQueryString.Parameter;

public class ModelHttpQueryStringBuilder
    extends ModelHttpQueryStringBuilderBase<ModelHttpQueryStringBuilder> {
  public ModelHttpQueryStringBuilder() {}

  public ModelHttpQueryStringBuilder(List<Parameter> parameters) {
    super(parameters);
  }

  public ModelHttpQueryStringBuilder(ModelHttpQueryString that) {
    super(that);
  }

  public ModelHttpQueryStringBuilder(ModelHttpQueryStringBuilder that) {
    super(that);
  }

  @Override
  public ModelHttpQueryString build() {
    return super.build();
  }
}
