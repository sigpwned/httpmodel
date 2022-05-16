package com.sigpwned.httpmodel;

import java.nio.charset.Charset;
import java.util.Objects;
import java.util.Optional;

public class ModelHttpMediaType {
  public static final ModelHttpMediaType APPLICATION_OCTET_STREAM =
      ModelHttpMediaType.of("application", "octet-stream");

  public static final ModelHttpMediaType APPLICATION_X_WWW_FORM_URLENCODED =
      ModelHttpMediaType.of("application", "x-www-form-urlencoded");

  public static ModelHttpMediaType of(String type, String subtype) {
    return new ModelHttpMediaType(type, subtype);
  }

  public static ModelHttpMediaType of(String type, String subtype, Charset charset) {
    return new ModelHttpMediaType(type, subtype, charset);
  }

  public static final String CHARSET = "charset";

  private static boolean isWildcard(String s) {
    return s.equals("*");
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
    this.type = type;
    this.subtype = subtype;
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
    return "HttpModelMediaType [type=" + type + ", subtype=" + subtype + ", charset=" + charset
        + "]";
  }
}
