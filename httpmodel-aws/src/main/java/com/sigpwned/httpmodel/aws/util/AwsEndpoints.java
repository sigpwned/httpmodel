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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.sigpwned.httpmodel.aws.AwsEndpoint;
import com.sigpwned.httpmodel.core.host.ModelHttpHostnameHost;

/**
 * https://docs.aws.amazon.com/general/latest/gr/rande.html
 */
public final class AwsEndpoints {
  private AwsEndpoints() {}

  private static final Pattern AMAZONAWS_COM_PATTERN = Pattern.compile(
      "([-a-z0-9]+?)(?:-fips)?\\.([-a-z0-9]+)\\.amazonaws\\.com", Pattern.CASE_INSENSITIVE);

  /**
   * https://docs.amazonaws.cn/en_us/aws/latest/userguide/endpoints-Beijing.html
   * https://docs.amazonaws.cn/en_us/aws/latest/userguide/endpoints-Ningxia.html
   */
  private static final Pattern AMAZONAWS_COM_CN_PATTERN = Pattern
      .compile("([-a-z0-9]+)\\.([-a-z0-9]+)\\.amazonaws\\.com\\.cn", Pattern.CASE_INSENSITIVE);

  /**
   * Dual-stack endpoints
   */
  private static final Pattern API_AWS_PATTERN =
      Pattern.compile("([-a-z0-9]+)\\.([-a-z0-9]+)\\.api\\.aws", Pattern.CASE_INSENSITIVE);

  private static final String EC2 = "ec2";

  private static final String EC2_AMAZONAWS_COM = EC2 + ".amazonaws.com";

  private static final String AUTOSCALING = "autoscaling";

  private static final String AUTOSCALING_AMAZONAWS_COM = AUTOSCALING + ".amazonaws.com";

  private static final String ELASTICMAPREDUCE = "elasticmapreduce";

  private static final String ELASTICMAPREDUCE_AMAZONAWS_COM = ELASTICMAPREDUCE + ".amazonaws.com";

  private static final String US_EAST_1 = "us-east-1";

  public static AwsEndpoint fromHostname(ModelHttpHostnameHost h) {
    return fromHostname(h.getHostname());
  }

  public static AwsEndpoint fromHostname(String s) {
    if (s.equals(EC2_AMAZONAWS_COM))
      return AwsEndpoint.of(US_EAST_1, EC2);
    if (s.equals(AUTOSCALING_AMAZONAWS_COM))
      return AwsEndpoint.of(US_EAST_1, AUTOSCALING);
    if (s.equals(ELASTICMAPREDUCE_AMAZONAWS_COM))
      return AwsEndpoint.of(US_EAST_1, ELASTICMAPREDUCE);

    for (Pattern p : new Pattern[] {AMAZONAWS_COM_PATTERN, AMAZONAWS_COM_CN_PATTERN,
        API_AWS_PATTERN}) {
      Matcher m = p.matcher(s);
      if (m.matches())
        return AwsEndpoint.of(m.group(2), m.group(1));
    }

    throw new IllegalArgumentException("unrecognized hostname " + s);
  }
}
