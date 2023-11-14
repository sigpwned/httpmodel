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

import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.Objects;
import com.sigpwned.httpmodel.core.model.ModelHttpHost;
import com.sigpwned.httpmodel.core.org.apache.commons.validator.routines.InetAddressValidator;

/**
 * Models an IPV4 host
 */
public class ModelHttpIpV4Host extends ModelHttpHost {
  public static ModelHttpIpV4Host fromString(String s) {
    if (!InetAddressValidator.getInstance().isValidInet4Address(s))
      throw new IllegalArgumentException("must be a valid IPv4 address of the form a.b.c.d");

    Inet4Address address;
    try {
      address = (Inet4Address) InetAddress.getByName(s);
    } catch (Exception e) {
      throw new IllegalArgumentException("not a valid IPv4 address", e);
    }

    return of(address);
  }

  public static ModelHttpIpV4Host of(Inet4Address address) {
    return new ModelHttpIpV4Host(address);
  }

  private Inet4Address address;

  public ModelHttpIpV4Host(Inet4Address address) {
    super(Type.IPV4);
    setAddress(address);
  }

  /**
   * @return the address
   */
  public Inet4Address getAddress() {
    return address;
  }

  public ModelHttpIpV4Host setAddress(Inet4Address address) {
    if (address == null)
      throw new NullPointerException();
    this.address = address;
    return this;
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
    ModelHttpIpV4Host other = (ModelHttpIpV4Host) obj;
    return Objects.equals(address, other.address);
  }

  @Override
  public String toString() {
    return getAddress().getHostAddress();
  }
}
