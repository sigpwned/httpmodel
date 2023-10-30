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

import java.net.Inet6Address;
import java.net.InetAddress;
import java.util.Objects;
import com.sigpwned.httpmodel.core.ModelHttpHost;
import com.sigpwned.httpmodel.core.org.apache.commons.validator.routines.InetAddressValidator;

/**
 * Validation code is based on Apache Commons Validator InetAddressValidator class.
 * 
 * @see <a href=
 *      "https://github.com/apache/commons-validator/blob/master/src/main/java/org/apache/commons/validator/routines/InetAddressValidator.java">https://github.com/apache/commons-validator/blob/master/src/main/java/org/apache/commons/validator/routines/InetAddressValidator.java</a>
 */
public class ModelHttpIpV6Host extends ModelHttpHost {
  public static ModelHttpIpV6Host fromString(String s) {
    if (!s.startsWith("[") || !s.endsWith("]"))
      throw new IllegalArgumentException("must be a valid IPv6 address surrounded by [ ]");

    s = s.substring(1, s.length() - 1);

    if (!InetAddressValidator.getInstance().isValidInet6Address(s))
      throw new IllegalArgumentException("must contain a valid IPv6 address");

    Inet6Address address;
    try {
      address = (Inet6Address) InetAddress.getByName(s);
    } catch (Exception e) {
      throw new IllegalArgumentException("not a valid IPv6 address", e);
    }

    return of(address);
  }

  public static ModelHttpIpV6Host of(Inet6Address address) {
    return new ModelHttpIpV6Host(address);
  }

  private final Inet6Address address;

  public ModelHttpIpV6Host(Inet6Address address) {
    super(Type.IPV6);
    this.address = address;
  }

  /**
   * @return the address
   */
  public Inet6Address getAddress() {
    return address;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + Objects.hash(address);
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
    ModelHttpIpV6Host other = (ModelHttpIpV6Host) obj;
    return Objects.equals(address, other.address);
  }

  @Override
  public String toString() {
    return "[" + getAddress().getHostAddress() + "]";
  }
}
