/*-
 * =================================LICENSE_START==================================
 * httpmodel-core
 * ====================================SECTION=====================================
 * Copyright (C) 2022 - 2023 Andy Boothe
 * ====================================SECTION=====================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ==================================LICENSE_END===================================
 */
package com.sigpwned.httpmodel.core.io.buffered;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.OptionalLong;
import com.sigpwned.httpmodel.core.io.BufferedInputStream;

public class FileBufferedInputStream extends BufferedInputStream {
  private final File file;
  private final boolean deleteOnClose;

  public FileBufferedInputStream(File file, boolean deleteOnClose) throws IOException {
    this.file = file;
    this.deleteOnClose = deleteOnClose;
    in = newInputStream();
  }

  @Override
  public OptionalLong length() throws IOException {
    return OptionalLong.of(file.length());
  }

  @Override
  public void close() throws IOException {
    try {
      super.close();
    } finally {
      if (deleteOnClose)
        file.delete();
    }
  }

  /**
   * hook
   */
  @Override
  protected InputStream newInputStream() throws IOException {
    return new FileInputStream(file);
  }
}
