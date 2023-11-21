package com.sigpwned.httpmodel.core.util;

import java.util.Optional;
import java.util.stream.Stream;

public final class MoreStreams {
  private MoreStreams() {}

  public static <T> Stream<T> stream(Optional<T> o) {
    return o.isPresent() ? Stream.of(o.get()) : Stream.empty();
  }
}
