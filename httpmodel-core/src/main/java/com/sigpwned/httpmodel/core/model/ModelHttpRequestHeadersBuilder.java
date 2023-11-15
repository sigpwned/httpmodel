package com.sigpwned.httpmodel.core.model;

import static java.util.Collections.emptyList;
import java.util.List;
import com.sigpwned.httpmodel.core.model.ModelHttpHeaders.Header;

public class ModelHttpRequestHeadersBuilder extends
    ModelHttpHeadersChildBuilderBase<ModelHttpRequestBuilder, ModelHttpRequestHeadersBuilder> {
  /* default */ ModelHttpRequestHeadersBuilder(ModelHttpRequestBuilder parent) {
    this(parent, emptyList());
  }

  /* default */ ModelHttpRequestHeadersBuilder(ModelHttpRequestBuilder parent,
      ModelHttpRequestHeadersBuilder that) {
    this(parent, that.headers());
  }

  /* default */ ModelHttpRequestHeadersBuilder(ModelHttpRequestBuilder parent,
      ModelHttpHeaders that) {
    this(parent, that.getHeaders());
  }

  /* default */ ModelHttpRequestHeadersBuilder(ModelHttpRequestBuilder parent,
      List<Header> headers) {
    super(parent, headers);
  }
}
