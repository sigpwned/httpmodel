package com.sigpwned.httpmodel.core.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Ignores all output
 */
public class NullOutputStream extends OutputStream {
  @Override
  public void write(int ch) throws IOException {}
}
