package com.sigpwned.httpmodel.core.model;

import java.util.List;
import com.sigpwned.httpmodel.core.model.ModelHttpHeaders.Header;

public class ModelHttpHeadersBuilder extends ModelHttpHeadersBuilderBase<ModelHttpHeadersBuilder> {
  public ModelHttpHeadersBuilder() {}

  public ModelHttpHeadersBuilder(List<Header> headers) {
    super(headers);
  }

  public ModelHttpHeadersBuilder(ModelHttpHeaders that) {
    super(that);
  }

  public ModelHttpHeadersBuilder(ModelHttpHeadersBuilder that) {
    super(that);
  }

  @Override
  public ModelHttpHeaders build() {
    return super.build();
  }
}
