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

import java.nio.charset.Charset;
import java.util.Objects;

/**
 * Models a MIME type.
 */
public class ModelHttpMediaTypeBuilder {
  private String type;
  private String subtype;
  private Charset charset;

  public ModelHttpMediaTypeBuilder() {}

  public ModelHttpMediaTypeBuilder(ModelHttpMediaTypeBuilder that) {
    if (that == null)
      throw new NullPointerException();
    this.type = that.type;
    this.subtype = that.subtype;
    this.charset = that.charset;
  }

  public ModelHttpMediaTypeBuilder(ModelHttpMediaType that) {
    if (that == null)
      throw new NullPointerException();
    this.type = that.getType();
    this.subtype = that.getSubtype();
    this.charset = that.getCharset().orElse(null);
  }

  public String type() {
    return type;
  }

  public ModelHttpMediaTypeBuilder type(String type) {
    this.type = type;
    return this;
  }

  public String subtype() {
    return subtype;
  }

  public ModelHttpMediaTypeBuilder subtype(String subtype) {
    this.subtype = subtype;
    return this;
  }

  public Charset charset() {
    return charset;
  }

  public ModelHttpMediaTypeBuilder charset(Charset charset) {
    this.charset = charset;
    return this;
  }

  public ModelHttpMediaType build() {
    return new ModelHttpMediaType(type, subtype, charset);
  }

  @Override
  public int hashCode() {
    return Objects.hash(charset, subtype, type);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ModelHttpMediaTypeBuilder other = (ModelHttpMediaTypeBuilder) obj;
    return Objects.equals(charset, other.charset) && Objects.equals(subtype, other.subtype)
        && Objects.equals(type, other.type);
  }

  @Override
  public String toString() {
    return "ModelHttpMediaTypeBuilder [type=" + type + ", subtype=" + subtype + ", charset="
        + charset + "]";
  }
}
