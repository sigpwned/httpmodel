package com.sigpwned.httpmodel.core.model;

import static java.util.Collections.emptyList;
import java.util.List;
import com.sigpwned.httpmodel.core.model.ModelHttpQueryString.Parameter;

public class ModelHttpRequestHeadUrlQueryStringBuilder extends
    ModelHttpQueryStringChildBuilderBase<ModelHttpRequestHeadUrlBuilder, ModelHttpRequestHeadUrlQueryStringBuilder> {
  /* default */ ModelHttpRequestHeadUrlQueryStringBuilder(ModelHttpRequestHeadUrlBuilder parent) {
    this(parent, emptyList());
  }

  /* default */ ModelHttpRequestHeadUrlQueryStringBuilder(ModelHttpRequestHeadUrlBuilder parent,
      ModelHttpRequestHeadUrlQueryStringBuilder that) {
    this(parent, that.parameters());
  }

  /* default */ ModelHttpRequestHeadUrlQueryStringBuilder(ModelHttpRequestHeadUrlBuilder parent,
      ModelHttpQueryString that) {
    this(parent, that.getParameters());
  }

  /* default */ ModelHttpRequestHeadUrlQueryStringBuilder(ModelHttpRequestHeadUrlBuilder parent,
      List<Parameter> parameters) {
    super(parent, parameters);
  }

  @Override
  protected ModelHttpQueryString build() {
    return super.build();
  }
}
