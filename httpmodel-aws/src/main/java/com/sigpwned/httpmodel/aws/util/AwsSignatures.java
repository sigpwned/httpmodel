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
package com.sigpwned.httpmodel.aws.util;

import java.io.IOException;
import com.sigpwned.httpmodel.aws.AwsSigningCredentials;
import com.sigpwned.httpmodel.aws.signer.SigV4AwsSigner;
import com.sigpwned.httpmodel.core.model.ModelHttpRequest;

public final class AwsSignatures {
  private AwsSignatures() {}

  public static ModelHttpRequest signedV4(String accessKeyId, String secretAccessKey,
      String sessionToken, ModelHttpRequest request) throws IOException {
    final AwsSigningCredentials credentials =
        AwsSigningCredentials.of(accessKeyId, secretAccessKey, sessionToken);
    return new SigV4AwsSigner(() -> credentials).sign(request);
  }
}
