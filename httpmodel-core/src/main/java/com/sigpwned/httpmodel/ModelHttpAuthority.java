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

import java.util.Objects;
import java.util.OptionalInt;
import java.util.regex.Pattern;

/**
 * Models a URL authority, which is the host and (optional) port.
 */
public class ModelHttpAuthority {
  private static final Pattern COLON = Pattern.compile(":");

  /**
   * Parses a valid authority, e.g., www.example.com:8080. The host should be a valid hostname, IPv4
   * address, or IPv6 address.
   * 
   * @throws IllegalArgumentException if the authority cannot be parsed
   * 
   * @see #toString()
   */
  public static ModelHttpAuthority fromString(String s) {
    String[] parts = COLON.split(s, 2);

    ModelHttpHost host = ModelHttpHost.fromString(s);

    if (parts.length == 1) {
      return of(host, OptionalInt.empty());
    } else {
      int port;
      try {
        port = Integer.parseInt(parts[1]);
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("port must be an integer");
      }
      return of(host, port);
    }
  }

  public static ModelHttpAuthority of(ModelHttpHost host) {
    return of(host, OptionalInt.empty());
  }

  public static ModelHttpAuthority of(ModelHttpHost host, OptionalInt port) {
    return new ModelHttpAuthority(host, port.isPresent() ? port.getAsInt() : null);
  }

  public static ModelHttpAuthority of(ModelHttpHost host, Integer port) {
    return new ModelHttpAuthority(host, port);
  }

  public static final int MIN_PORT = 0;

  public static final int MAX_PORT = 65535;

  private final ModelHttpHost host;

  private final Integer port;

  public ModelHttpAuthority(ModelHttpHost host, Integer port) {
    if (host == null)
      throw new NullPointerException();
    if (port != null && (port < MIN_PORT || port > MAX_PORT))
      throw new IllegalArgumentException("port must be between " + MIN_PORT + " and " + MAX_PORT);
    this.host = host;
    this.port = port;
  }

  /**
   * @return the host
   */
  public ModelHttpHost getHost() {
    return host;
  }

  /**
   * @return the port
   */
  public OptionalInt getPort() {
    return port != null ? OptionalInt.of(port.intValue()) : OptionalInt.empty();
  }

  @Override
  public int hashCode() {
    return Objects.hash(host, port);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ModelHttpAuthority other = (ModelHttpAuthority) obj;
    return Objects.equals(host, other.host) && Objects.equals(port, other.port);
  }

  /**
   * Returns a stringized form of this authority, e.g., www.example.com:8080.
   * 
   * @see #fromString(String)
   */
  @Override
  public String toString() {
    String result = getHost().toString();
    if (getPort().isPresent())
      result = result + ":" + getPort().getAsInt();
    return result;
  }
}
