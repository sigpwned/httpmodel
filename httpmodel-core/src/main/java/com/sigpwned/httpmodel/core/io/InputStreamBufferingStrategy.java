package com.sigpwned.httpmodel.core.io;

@FunctionalInterface
public interface InputStreamBufferingStrategy {
  public static InputStreamBufferingStrategy DEFAULT = InputStreamBufferer::newDefaultInstance;

  public InputStreamBufferer newBufferer();
}
