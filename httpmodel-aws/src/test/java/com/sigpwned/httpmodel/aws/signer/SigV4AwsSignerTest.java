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
package com.sigpwned.httpmodel.aws.signer;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.Test;
import com.sigpwned.httpmodel.aws.AwsSigningCredentials;
import com.sigpwned.httpmodel.core.model.ModelHttpHeaders;
import com.sigpwned.httpmodel.core.model.ModelHttpRequest;
import com.sigpwned.httpmodel.core.model.ModelHttpUrl;

@SuppressWarnings("deprecation")
public class SigV4AwsSignerTest {
  public static final String AWS_ACCESS_KEY_ID = "AKIAIOSFODNN7EXAMPLE";

  public static final String AWS_SECRET_ACCESS_KEY = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY";

  public static final String US_EAST_1 = "us-east-1";

  public static final String S3 = "s3";

  /**
   * https://docs.aws.amazon.com/AmazonS3/latest/API/sig-v4-header-based-auth.html
   */
  @Test
  public void test() throws IOException {
    final AwsSigningCredentials credentials =
        AwsSigningCredentials.of(AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY, null);

    final ModelHttpRequest request = ModelHttpRequest.builder().version("1.1").method("GET")
        .headers(ModelHttpHeaders.of())
        .url(ModelHttpUrl.fromString("https://examplebucket.s3.amazonaws.com/?max-keys=2&prefix=J"))
        .build();

    final SigV4AwsSigner signer = new SigV4AwsSigner(() -> credentials);

    final OffsetDateTime now =
        OffsetDateTime.of(LocalDate.of(2013, 5, 24), LocalTime.MIDNIGHT, ZoneOffset.UTC);

    String authorization = signer.computeAuthorizationHeaderValue(request, US_EAST_1, S3, now);

    assertThat(authorization, is(
        "AWS4-HMAC-SHA256 Credential=AKIAIOSFODNN7EXAMPLE/20130524/us-east-1/s3/aws4_request,SignedHeaders=host;x-amz-content-sha256;x-amz-date,Signature=34b48302e7b5fa45bde8084f4b7868a86f0a534bc59db6670ed5711ef69dc6f7"));
  }
}
