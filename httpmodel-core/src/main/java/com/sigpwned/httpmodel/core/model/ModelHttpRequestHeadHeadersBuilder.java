package com.sigpwned.httpmodel.core.model;

import static java.util.Collections.emptyList;
import java.util.List;
import com.sigpwned.httpmodel.core.model.ModelHttpHeaders.Header;

public class ModelHttpRequestHeadHeadersBuilder extends
    ModelHttpHeadersChildBuilderBase<ModelHttpRequestHeadBuilder, ModelHttpRequestHeadHeadersBuilder> {
  /* default */ ModelHttpRequestHeadHeadersBuilder(ModelHttpRequestHeadBuilder parent) {
    this(parent, emptyList());
  }

  /* default */ ModelHttpRequestHeadHeadersBuilder(ModelHttpRequestHeadBuilder parent,
      ModelHttpRequestHeadHeadersBuilder that) {
    this(parent, that.headers());
  }

  /* default */ ModelHttpRequestHeadHeadersBuilder(ModelHttpRequestHeadBuilder parent,
      ModelHttpHeaders that) {
    this(parent, that.getHeaders());
  }

  /* default */ ModelHttpRequestHeadHeadersBuilder(ModelHttpRequestHeadBuilder parent,
      List<Header> headers) {
    super(parent, headers);
  }
}
