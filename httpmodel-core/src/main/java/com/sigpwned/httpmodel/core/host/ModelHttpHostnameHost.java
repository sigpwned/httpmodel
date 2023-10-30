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
package com.sigpwned.httpmodel.core.host;

import java.util.Objects;
import java.util.regex.Pattern;
import com.sigpwned.httpmodel.core.ModelHttpHost;

/**
 * Models an HTTP hostname. Only validates for form. In particular, does no DNS requests to
 * validate.
 */
public class ModelHttpHostnameHost extends ModelHttpHost {
  /**
   * Validates for form only. Does not validate length, etc.
   */
  public static final Pattern HOSTNAME =
      Pattern.compile("^[-a-z0-9]+(?:[.][-a-z0-9]+)*$", Pattern.CASE_INSENSITIVE);

  /**
   * Parses a valid DNS hostname. Validates for form only. In particular, does no DNS requests to
   * validate.
   * 
   * @throws IllegalArgumentException if the given string is not a valid hostname
   * 
   * @see #HOSTNAME
   * @see #toString()
   */
  public static ModelHttpHostnameHost fromString(String s) {
    return new ModelHttpHostnameHost(s);
  }

  public static ModelHttpHostnameHost of(String hostname) {
    return new ModelHttpHostnameHost(hostname);
  }

  private final String hostname;

  public ModelHttpHostnameHost(String hostname) {
    super(Type.HOSTNAME);
    if (hostname == null)
      throw new NullPointerException();
    if (!HOSTNAME.matcher(hostname).matches())
      throw new IllegalArgumentException("hostname must be a valid DNS hostname");
    this.hostname = hostname;
  }

  /**
   * @return the hostname
   */
  public String getHostname() {
    return hostname;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + Objects.hash(hostname);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!super.equals(obj))
      return false;
    if (getClass() != obj.getClass())
      return false;
    ModelHttpHostnameHost other = (ModelHttpHostnameHost) obj;
    return Objects.equals(hostname, other.hostname);
  }

  /**
   * Returns the hostname
   * 
   * @see #fromString(String)
   */
  @Override
  public String toString() {
    return getHostname();
  }
}
