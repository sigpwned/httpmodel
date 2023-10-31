/*-
 * =================================LICENSE_START==================================
 * httpmodel-aws
 * ====================================SECTION=====================================
 * Copyright (C) 2022 - 2023 Andy Boothe
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
package com.sigpwned.httpmodel.aws;

import java.util.Objects;

public class AwsEndpoint {
  public static AwsEndpoint of(String region, String service) {
    return new AwsEndpoint(region, service);
  }

  private final String region;

  private final String service;

  public AwsEndpoint(String region, String service) {
    if (region == null)
      throw new NullPointerException();
    if (service == null)
      throw new NullPointerException();
    this.region = region;
    this.service = service.toLowerCase();
  }

  public String getRegion() {
    return region;
  }

  public String getService() {
    return service;
  }

  @Override
  public int hashCode() {
    return Objects.hash(region, service);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    AwsEndpoint other = (AwsEndpoint) obj;
    return Objects.equals(region, other.region) && Objects.equals(service, other.service);
  }

  @Override
  public String toString() {
    return "AwsEndpoint [region=" + region + ", service=" + service + "]";
  }
}
