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

import static java.util.stream.Collectors.toMap;
import java.nio.charset.Charset;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModelHttpMediaType {
  private static final Logger LOGGER = LoggerFactory.getLogger(ModelHttpMediaType.class);

  public static final ModelHttpMediaType APPLICATION_OCTET_STREAM =
      ModelHttpMediaType.of("application", "octet-stream");

  public static final ModelHttpMediaType APPLICATION_X_WWW_FORM_URLENCODED =
      ModelHttpMediaType.of("application", "x-www-form-urlencoded");

  public static final String WILDCARD = "*";

  private static final String CHARSET = "charset";

  private static final Pattern SEMICOLON = Pattern.compile(";");

  private static final Pattern SLASH = Pattern.compile("/");

  private static final Pattern COMMA = Pattern.compile(",");

  private static final Pattern EQUALS = Pattern.compile("=");

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
          if (LOGGER.isDebugEnabled())
            LOGGER.debug("Ignoring invalid charset {}", charsetName, e);
          charset = null;
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
    this.type = type.toLowerCase();
    this.subtype = subtype.toLowerCase();
    this.charset = charset;
  }

  /**
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * @return the subtype
   */
  public String getSubtype() {
    return subtype;
  }

  public Optional<Charset> getCharset() {
    return Optional.ofNullable(charset);
  }

  public ModelHttpMediaType withCharset(Charset charset) {
    return new ModelHttpMediaType(getType(), getSubtype(), charset);
  }

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

  @Override
  public String toString() {
    String result = getType() + "/" + getSubtype();
    if (getCharset().isPresent()) {
      result = result + "; charset=" + getCharset().get().name();
    }
    return result;
  }
}
