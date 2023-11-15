package com.sigpwned.httpmodel.core.model;

import static java.util.Collections.emptyList;
import java.util.List;
import com.sigpwned.httpmodel.core.model.ModelHttpQueryString.Parameter;

public class ModelHttpRequestUrlQueryStringBuilder extends
    ModelHttpQueryStringChildBuilderBase<ModelHttpRequestUrlBuilder, ModelHttpRequestUrlQueryStringBuilder> {
  /* default */ ModelHttpRequestUrlQueryStringBuilder(ModelHttpRequestUrlBuilder parent) {
    this(parent, emptyList());
  }

  /* default */ ModelHttpRequestUrlQueryStringBuilder(ModelHttpRequestUrlBuilder parent,
      ModelHttpRequestUrlQueryStringBuilder that) {
    this(parent, that.parameters());
  }

  /* default */ ModelHttpRequestUrlQueryStringBuilder(ModelHttpRequestUrlBuilder parent,
      ModelHttpQueryString that) {
    this(parent, that.getParameters());
  }

  /* default */ ModelHttpRequestUrlQueryStringBuilder(ModelHttpRequestUrlBuilder parent,
      List<Parameter> parameters) {
    super(parent, parameters);
  }

  @Override
  protected ModelHttpQueryString build() {
    return super.build();
  }
}
