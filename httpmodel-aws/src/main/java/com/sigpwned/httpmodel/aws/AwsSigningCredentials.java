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
import java.util.Optional;

public class AwsSigningCredentials {
  public static AwsSigningCredentials of(String accessKeyId, String secretAccessKey,
      String sessionToken) {
    return new AwsSigningCredentials(accessKeyId, secretAccessKey, sessionToken);
  }

  private final String accessKeyId;
  private final String secretAccessKey;
  private final String sessionToken;

  public AwsSigningCredentials(String accessKeyId, String secretAccessKey, String sessionToken) {
    if (accessKeyId == null)
      throw new NullPointerException();
    if (secretAccessKey == null)
      throw new NullPointerException();
    this.accessKeyId = accessKeyId;
    this.secretAccessKey = secretAccessKey;
    this.sessionToken = sessionToken;
  }

  public String getAccessKeyId() {
    return accessKeyId;
  }

  public String getSecretAccessKey() {
    return secretAccessKey;
  }

  public Optional<String> getSessionToken() {
    return Optional.ofNullable(sessionToken);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accessKeyId, secretAccessKey, sessionToken);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    AwsSigningCredentials other = (AwsSigningCredentials) obj;
    return Objects.equals(accessKeyId, other.accessKeyId)
        && Objects.equals(secretAccessKey, other.secretAccessKey)
        && Objects.equals(sessionToken, other.sessionToken);
  }

  @Override
  public String toString() {
    return "AwsSigningCredentials [accessKeyId=" + accessKeyId + ", secretAccessKey="
        + secretAccessKey + ", sessionToken=" + sessionToken + "]";
  }
}