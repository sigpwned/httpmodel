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

/**
 * Models a URL authority, which is the host and (optional) port.
 */
public class ModelHttpAuthorityBuilder {
  private ModelHttpHost host;
  private Integer port;

  public ModelHttpAuthorityBuilder() {}

  public ModelHttpAuthorityBuilder(ModelHttpAuthorityBuilder that) {
    if (that == null)
      throw new NullPointerException();
    this.host = that.host();
    this.port = that.port();
  }

  public ModelHttpAuthorityBuilder(ModelHttpAuthority that) {
    if (that == null)
      throw new NullPointerException();
    this.host = that.getHost();
    this.port = that.getPort();
  }

  public ModelHttpHost host() {
    return host;
  }

  public ModelHttpAuthorityBuilder host(ModelHttpHost host) {
    this.host = host;
    return this;
  }

  public Integer port() {
    return port;
  }

  public ModelHttpAuthorityBuilder port(Integer port) {
    this.port = port;
    return this;
  }

  public ModelHttpAuthority build() {
    return new ModelHttpAuthority(host(), port());
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
    ModelHttpAuthorityBuilder other = (ModelHttpAuthorityBuilder) obj;
    return Objects.equals(host, other.host) && Objects.equals(port, other.port);
  }

  @Override
  public String toString() {
    return "ModelHttpAuthorityBuilder [host=" + host + ", port=" + port + "]";
  }
}
