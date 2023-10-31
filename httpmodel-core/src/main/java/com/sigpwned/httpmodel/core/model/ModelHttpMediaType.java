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

import static java.util.stream.Collectors.toMap;
import java.nio.charset.Charset;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Models a MIME type.
 */
public class ModelHttpMediaType {
  public static ModelHttpMediaTypeBuilder builder() {
    return new ModelHttpMediaTypeBuilder();
  }

  public static final String WILDCARD = "*";

  private static final String CHARSET = "charset";

  private static final Pattern SEMICOLON = Pattern.compile(";");

  private static final Pattern SLASH = Pattern.compile("/");

  private static final Pattern COMMA = Pattern.compile(",");

  private static final Pattern EQUALS = Pattern.compile("=");

  /**
   * Parses a valid mime type, e.g., text/plain; charset=utf-8.
   *
   * @throws IllegalArgumentException if the MIME type cannot be parsed
   *
   * @see #toString()
   */
  public static ModelHttpMediaType fromString(String s) {
    String[] parts = SEMICOLON.split(s.trim(), 2);

    String typesString = parts[0];
    String paramsString = parts.length == 2 ? parts[1].trim() : null;

    String[] types = SLASH.split(typesString, 2);
    if (types.length != 2)
      throw new IllegalArgumentException("not enough parts");

    String type = types[0].toLowerCase();
    String subtype = types[1].toLowerCase();

    Charset charset;
    if (paramsString != null && !paramsString.isEmpty()) {
      Map<String, String> params = COMMA.splitAsStream(paramsString).map(String::trim).map(nv -> {
        String[] ps = EQUALS.split(nv, 2);
        String name = ps[0].toLowerCase();
        String value = ps.length == 2 ? ps[1].trim() : null;
        return new AbstractMap.SimpleImmutableEntry<String, String>(name, value);
      }).collect(toMap(e -> e.getKey(), e -> e.getValue(), (a, b) -> a));

      String charsetName = params.get(CHARSET);

      if (charsetName != null) {
        try {
          charset = Charset.forName(charsetName);
        } catch (Exception e) {
          throw new IllegalArgumentException("charset must have valid charset name");
        }
      } else {
        charset = null;
      }
    } else {
      charset = null;
    }

    return ModelHttpMediaType.of(type, subtype, charset);
  }

  public static ModelHttpMediaType of(String type, String subtype) {
    return new ModelHttpMediaType(type, subtype);
  }

  public static ModelHttpMediaType of(String type, String subtype, Charset charset) {
    return new ModelHttpMediaType(type, subtype, charset);
  }

  private static boolean isWildcard(String s) {
    return s.equals(WILDCARD);
  }

  private final String type;
  private final String subtype;
  private final Charset charset;

  public ModelHttpMediaType(String type, String subtype) {
    this(type, subtype, null);
  }

  public ModelHttpMediaType(String type, String subtype, Charset charset) {
    if (type == null)
      throw new NullPointerException();
    if (subtype == null)
      throw new NullPointerException();
    if (isWildcard(type) && !isWildcard(subtype))
      throw new IllegalArgumentException("wildcard type requires wildcard subtype");
    this.type = type.toLowerCase();
    this.subtype = subtype.toLowerCase();
    this.charset = charset;
  }

  /* default */ ModelHttpMediaType(ModelHttpMediaTypeBuilder that) {
    this(that.type(), that.subtype(), that.charset());
  }

  /**
   * @return the type
   */
  public String getType() {
    return type;
  }

  public ModelHttpMediaType withType(String type) {
    return toBuilder().type(type).build();
  }

  /**
   * @return the subtype
   */
  public String getSubtype() {
    return subtype;
  }

  public ModelHttpMediaType withSubtype(String subtype) {
    return toBuilder().subtype(subtype).build();
  }

  public Optional<Charset> getCharset() {
    return Optional.ofNullable(charset);
  }

  public ModelHttpMediaType withCharset(Charset charset) {
    return toBuilder().charset(charset).build();
  }

  /**
   * Considers subtype and type only. Parameters are ignored.
   */
  public boolean isCompatible(ModelHttpMediaType that) {
    if (this.getType().equals(that.getType()) || isWildcard(this.getType())
        || isWildcard(that.getType())) {
      if (this.getSubtype().equals(that.getSubtype()) || isWildcard(this.getSubtype())
          || isWildcard(that.getSubtype())) {
        return true;
      }
    }
    return false;
  }

  public ModelHttpMediaTypeBuilder toBuilder() {
    return new ModelHttpMediaTypeBuilder(this);
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
    ModelHttpMediaType other = (ModelHttpMediaType) obj;
    return Objects.equals(charset, other.charset) && Objects.equals(subtype, other.subtype)
        && Objects.equals(type, other.type);
  }

  /**
   * Returns a valid mime type string for this object
   *
   * @see #fromString(String)
   */
  @Override
  public String toString() {
    String result = getType() + "/" + getSubtype();
    if (getCharset().isPresent()) {
      result = result + "; charset=" + getCharset().get().name();
    }
    return result;
  }
}
