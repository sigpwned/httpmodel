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

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import com.sigpwned.httpmodel.core.ModelHttpEntity;
import com.sigpwned.httpmodel.core.entity.ModelHttpFormData.Entry;
import com.sigpwned.httpmodel.core.util.ModelHttpEncodings;
import com.sigpwned.httpmodel.core.util.ModelHttpMediaTypes;

/**
 * Models an HTTP entity of type application/x-www-form-urlencoded.
 */
public class ModelHttpFormData implements Iterable<Entry> {
  private static final Pattern AMPERSAND = Pattern.compile("&");

  private static final Pattern EQUALS = Pattern.compile("=");

  /**
   * Models a single form entry, e.g., alpha=bravo
   */
  public static class Entry {
    /**
     * Parses a valid form entry. The name and value are automatically urldecoded. If the form entry
     * does not contain "=", then the value is considered absent.
     * 
     * @throws IllegalArgumentException if the form entry is not valid
     * 
     * @see ModelHttpEncodings#urldecode(String)
     * @see #toString()
     */
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
    public Optional<String> getValue() {
      return Optional.ofNullable(value);
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

    /**
     * Converts this object into a valid form entry string. The name and value are automatically
     * urlencoded.
     * 
     * @see ModelHttpEncodings#urlencode(String)
     * @see #fromString(String)
     */
    @Override
    public String toString() {
      String result = ModelHttpEncodings.urlencode(getName());
      if (getValue().isPresent())
        result = result + "=" + ModelHttpEncodings.urlencode(getValue().get());
      return result;
    }
  }

  /**
   * Convenience method that converts the given entity to a string and then parses
   * 
   * @see #fromString(String)
   */
  public static ModelHttpFormData fromEntity(ModelHttpEntity entity) {
    return fromString(entity.toString(StandardCharsets.UTF_8));
  }

  /**
   * Parses a valid form string, e.g., alpha=bravo&amp;charlie=delta
   * 
   * @throws IllegalArgumentException if the form string is not valid
   *
   * @see Entry#fromString(String)
   * @see #toString()
   */
  public static ModelHttpFormData fromString(String s) {
    return ModelHttpFormData.of(AMPERSAND.splitAsStream(s).filter(nv -> !nv.isEmpty())
        .map(Entry::fromString).collect(toList()));
  }

  public static ModelHttpFormData of(Entry... entries) {
    return new ModelHttpFormData(asList(entries));
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

  public Optional<Entry> findFirstEntryByName(String name) {
    return stream().filter(e -> e.getName().equals(name)).findFirst();
  }

  public List<Entry> findAllEntriesByName(String name) {
    return stream().filter(e -> e.getName().equals(name)).collect(toList());
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

  public ModelHttpEntity toEntity() {
    return ModelHttpEntity.of(ModelHttpMediaTypes.APPLICATION_X_WWW_FORM_URLENCODED, toString());
  }

  /**
   * Converts this object to a valid form string
   * 
   * @see #fromString(String)
   */
  @Override
  public String toString() {
    return stream().map(Entry::toString).collect(joining("&"));
  }

  @Override
  public Iterator<Entry> iterator() {
    return getEntries().iterator();
  }

  public Stream<Entry> stream() {
    return getEntries().stream();
  }
}
