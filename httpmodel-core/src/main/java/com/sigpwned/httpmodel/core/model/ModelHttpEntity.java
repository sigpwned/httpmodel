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
package com.sigpwned.httpmodel.core.model;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import com.sigpwned.httpmodel.core.util.ModelHttpMediaTypes;
import com.sigpwned.httpmodel.core.util.MoreByteStreams;
import com.sigpwned.httpmodel.core.util.MoreCharStreams;

/**
 * Models an HTTP entity, which is a byte stream and content type.
 *
 * Note that this class buffers the entire entity in memory. As a result, it's important only to use
 * this class for modeling HTTP flows when the approximate size of the response is known.
 */
public class ModelHttpEntity implements AutoCloseable {
  @FunctionalInterface
  public static interface EntityStreamProvider extends AutoCloseable {
    public InputStream newInputStream() throws IOException;

    @Override
    default void close() throws IOException {}
  }

  public static ModelHttpEntity ofPlainText(String text) {
    return of(ModelHttpMediaTypes.TEXT_PLAIN, text);
  }

  public static ModelHttpEntity of(ModelHttpMediaType type, String data) {
    return new ModelHttpEntity(type, data);
  }

  public static ModelHttpEntity of(ModelHttpMediaType type, byte[] data) {
    return new ModelHttpEntity(type, data);
  }

  public static ModelHttpEntity of(ModelHttpMediaType type, File data) {
    return of(type, data, false);
  }

  public static ModelHttpEntity of(ModelHttpMediaType type, File data, boolean deleteOnClose) {
    return new ModelHttpEntity(type, new EntityStreamProvider() {
      @Override
      public InputStream newInputStream() throws IOException {
        return new FileInputStream(data);
      }

      @Override
      public void close() throws IOException {
        if (deleteOnClose)
          data.delete();
      }
    });
  }

  private final ModelHttpMediaType type;
  private final EntityStreamProvider streamProvider;

  public ModelHttpEntity(ModelHttpMediaType type, String data) {
    this(type.withCharset(StandardCharsets.UTF_8), data.getBytes(StandardCharsets.UTF_8));
  }

  public ModelHttpEntity(ModelHttpMediaType type, byte[] data) {
    this(type, () -> new ByteArrayInputStream(data));
  }

  public ModelHttpEntity(ModelHttpMediaType type, EntityStreamProvider streamProvider) {
    if (streamProvider == null)
      throw new NullPointerException();
    this.type = type;
    this.streamProvider = streamProvider;
  }

  /**
   * @return the type
   */
  public Optional<ModelHttpMediaType> getType() {
    return Optional.ofNullable(type);
  }

  public InputStream readBytes() throws IOException {
    return getStreamProvider().newInputStream();
  }

  public byte[] toByteArray() throws IOException {
    return MoreByteStreams.toByteArray(readBytes());
  }

  public Reader readChars(Charset defaultCharset) throws IOException {
    return new InputStreamReader(readBytes(),
        getType().flatMap(ModelHttpMediaType::getCharset).orElse(defaultCharset));
  }

  public String toString(Charset defaultCharset) throws IOException {
    return MoreCharStreams.toString(readChars(defaultCharset));
  }

  @Override
  public void close() throws IOException {
    getStreamProvider().close();
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

  private EntityStreamProvider getStreamProvider() {
    return streamProvider;
  }
}
