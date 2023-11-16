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
package com.sigpwned.httpmodel.core.entity;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import com.sigpwned.httpmodel.core.io.BufferedInputStream;
import com.sigpwned.httpmodel.core.io.buffered.MemoryBufferedInputStream;
import com.sigpwned.httpmodel.core.model.ModelHttpEntity;
import com.sigpwned.httpmodel.core.model.ModelHttpEntityInputStream;
import com.sigpwned.httpmodel.core.model.ModelHttpMediaType;
import com.sigpwned.httpmodel.core.util.ModelHttpMediaTypes;

/**
 * Models an HTTP entity of type application/x-www-form-urlencoded.
 */
public class PlainTextModelHttpEntity implements ModelHttpEntity {
  public static final ModelHttpMediaType CONTENT_TYPE =
      ModelHttpMediaTypes.TEXT_PLAIN.withCharset(StandardCharsets.UTF_8);

  public static final PlainTextModelHttpEntity EMPTY = new PlainTextModelHttpEntity("");

  /**
   * Convenience method that converts the given entity to a string and then parses
   *
   * @see #fromString(String)
   */
  public static PlainTextModelHttpEntity fromEntity(ModelHttpEntityInputStream entity)
      throws IOException {
    return fromString(entity.toString(StandardCharsets.UTF_8));
  }

  public static PlainTextModelHttpEntity fromString(String text) {
    return PlainTextModelHttpEntity.of(text);
  }

  public static PlainTextModelHttpEntity of(String text) {
    if (text == null)
      throw new NullPointerException();
    return text.isEmpty() ? EMPTY : new PlainTextModelHttpEntity(text);
  }

  private final String text;

  public PlainTextModelHttpEntity(String text) {
    if (text == null)
      throw new NullPointerException();
    this.text = text;
  }

  public String getText() {
    return text;
  }

  @Override
  public int hashCode() {
    return Objects.hash(text);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    PlainTextModelHttpEntity other = (PlainTextModelHttpEntity) obj;
    return Objects.equals(text, other.text);
  }

  @Override
  public ModelHttpMediaType getContentType() {
    return CONTENT_TYPE;
  }

  @Override
  public BufferedInputStream toInputStream() {
    return new MemoryBufferedInputStream(toString(), StandardCharsets.UTF_8);
  }

  /**
   * Converts this object to a valid form string
   *
   * @see #fromString(String)
   */
  @Override
  public String toString() {
    return getText();
  }
}
