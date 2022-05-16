package com.sigpwned.httpmodel.entity;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import com.sigpwned.httpmodel.ModelHttpEntity;
import com.sigpwned.httpmodel.util.ModelHttpEncodings;

public class ModelHttpFormData {
  public static class Entry {
    public static Entry fromString(String nv) {
      String[] parts = EQUALS.split(nv, 2);
      String n = ModelHttpEncodings.urldecode(parts[0]);
      String v = parts.length == 2 ? ModelHttpEncodings.urldecode(parts[1]) : "";
      return Entry.of(n, v);
    }

    public static Entry of(String name, String value) {
      return new Entry(name, value);
    }

    private final String name;
    private final String value;

    public Entry(String name, String value) {
      if (name == null)
        throw new NullPointerException();
      if (value == null)
        throw new NullPointerException();
      this.name = name;
      this.value = value;
    }

    /**
     * @return the name
     */
    public String getName() {
      return name;
    }

    /**
     * @return the value
     */
    public String getValue() {
      return value;
    }

    @Override
    public int hashCode() {
      return Objects.hash(name, value);
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      Entry other = (Entry) obj;
      return Objects.equals(name, other.name) && Objects.equals(value, other.value);
    }

    @Override
    public String toString() {
      return new StringBuilder().append(ModelHttpEncodings.urlencode(getName())).append("=")
          .append(ModelHttpEncodings.urlencode(getValue())).toString();
    }
  }

  public static ModelHttpFormData fromEntity(ModelHttpEntity entity) {
    return fromString(entity.toString(StandardCharsets.UTF_8));
  }

  private static final Pattern AMPERSAND = Pattern.compile("&");

  private static final Pattern EQUALS = Pattern.compile("=");

  public static ModelHttpFormData fromString(String s) {
    return ModelHttpFormData.of(AMPERSAND.splitAsStream(s).filter(nv -> !nv.isEmpty())
        .map(Entry::fromString).collect(toList()));
  }

  public static ModelHttpFormData of(List<Entry> entries) {
    return new ModelHttpFormData(entries);
  }

  private final List<Entry> entries;

  public ModelHttpFormData(List<Entry> entries) {
    if (entries == null)
      throw new NullPointerException();
    this.entries = unmodifiableList(entries);
  }

  /**
   * @return the entries
   */
  public List<Entry> getEntries() {
    return entries;
  }

  @Override
  public int hashCode() {
    return Objects.hash(entries);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ModelHttpFormData other = (ModelHttpFormData) obj;
    return Objects.equals(entries, other.entries);
  }

  @Override
  public String toString() {
    return getEntries().stream().map(Entry::toString).collect(joining("&"));
  }
}
