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

import java.util.Objects;
import com.sigpwned.httpmodel.core.host.ModelHttpHostnameHost;
import com.sigpwned.httpmodel.core.host.ModelHttpIpV4Host;
import com.sigpwned.httpmodel.core.host.ModelHttpIpV6Host;

/**
 * Models an HTTP URL host. Must be one of hostname, IPv4 address host, or IPv6 address host.
 * 
 * @see ModelHttpHostnameHost
 * @see ModelHttpIpV4Host
 * @see ModelHttpIpV6Host
 */
public abstract class ModelHttpHost {
  /**
   * Parses a valid HTTP URL host from the given string.
   * 
   * @see ModelHttpHostnameHost#fromString(String)
   * @see ModelHttpIpV4Host#fromString(String)
   * @see ModelHttpIpV6Host#fromString(String)
   */
  public static ModelHttpHost fromString(String s) {
    ModelHttpHost result = null;

    if (result == null) {
      try {
        result = ModelHttpIpV6Host.fromString(s);
      } catch (IllegalArgumentException e) {
        // This is fine. It's just not an IPv6 address.
      }
    }

    if (result == null) {
      try {
        result = ModelHttpIpV4Host.fromString(s);
      } catch (IllegalArgumentException e) {
        // This is fine. It's just not an IPv4 address.
      }
    }

    if (result == null) {
      try {
        result = ModelHttpHostnameHost.fromString(s);
      } catch (IllegalArgumentException e) {
        // This is fine. It's just not a hostname address.
      }
    }

    if (result == null)
      throw new IllegalArgumentException("must be a valid hostname, IPv4 host, or IPv6 host");

    return result;
  }

  public static enum Type {
    HOSTNAME, IPV4, IPV6;
  }

  private final Type type;

  public ModelHttpHost(Type type) {
    this.type = type;
  }

  /**
   * @return the type
   */
  public Type getType() {
    return type;
  }

  public ModelHttpHostnameHost asHostname() {
    return (ModelHttpHostnameHost) this;
  }

  public ModelHttpIpV4Host asIpV4() {
    return (ModelHttpIpV4Host) this;
  }

  public ModelHttpIpV6Host asIpV6() {
    return (ModelHttpIpV6Host) this;
  }

  @Override
  public int hashCode() {
    return Objects.hash(type);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ModelHttpHost other = (ModelHttpHost) obj;
    return type == other.type;
  }

  /**
   * Returns the given host in a valid stringized form
   */
  public abstract String toString();
}
