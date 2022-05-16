/*-
 * =================================LICENSE_START==================================
 * httpmodel-core
 * ====================================SECTION=====================================
 * Copyright (C) 2022 Andy Boothe
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
package com.sigpwned.httpmodel;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import com.sigpwned.httpmodel.util.MoreByteStreams;
import com.sigpwned.httpmodel.util.MoreCharStreams;

public class ModelHttpEntity {
  public static ModelHttpEntity of(ModelHttpMediaType type, byte[] data) {
    return new ModelHttpEntity(type, data);
  }

  private final ModelHttpMediaType type;

  private final byte[] data;

  public ModelHttpEntity(ModelHttpMediaType type, byte[] data) {
    if (data == null)
      throw new NullPointerException();
    this.type = type;
    this.data = data;
  }

  /**
   * @return the type
   */
  public Optional<ModelHttpMediaType> getType() {
    return Optional.ofNullable(type);
  }

  /**
   * @return the data
   */
  private byte[] getData() {
    return data;
  }

  public InputStream readBytes() {
    return new ByteArrayInputStream(getData());
  }

  public byte[] toByteArray() {
    try {
      return MoreByteStreams.toByteArray(readBytes());
    } catch (IOException e) {
      // This should never happen since it's all in memory
      throw new UncheckedIOException(e);
    }
  }

  public Reader readChars(Charset defaultCharset) {
    return new InputStreamReader(readBytes(),
        getType().flatMap(ModelHttpMediaType::getCharset).orElse(defaultCharset));
  }

  public String toString(Charset defaultCharset) {
    try {
      return MoreCharStreams.toString(readChars(defaultCharset));
    } catch (IOException e) {
      // Should never happen since this is all in memory
      throw new UncheckedIOException(e);
    }
  }

  public int length() {
    return getData().length;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Arrays.hashCode(data);
    result = prime * result + Objects.hash(type);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ModelHttpEntity other = (ModelHttpEntity) obj;
    return Arrays.equals(data, other.data) && Objects.equals(type, other.type);
  }

  @Override
  public String toString() {
    return "ModelHttpEntity [type=" + type + ", data=" + Arrays.toString(data) + "]";
  }
}
