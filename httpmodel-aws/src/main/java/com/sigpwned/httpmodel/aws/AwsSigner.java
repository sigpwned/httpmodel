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

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import com.sigpwned.httpmodel.aws.util.AwsEndpoints;
import com.sigpwned.httpmodel.core.model.ModelHttpRequest;

public interface AwsSigner {
  default ModelHttpRequest sign(ModelHttpRequest request) {
    AwsEndpoint endpoint = AwsEndpoints
        .fromHostname(request.getUrl().getAuthority().getHost().asHostname().getHostname());
    return sign(request, endpoint.getRegion(), endpoint.getService());
  }

  default ModelHttpRequest sign(ModelHttpRequest request, String region, String service) {
    return sign(request, region, service, OffsetDateTime.now(ZoneOffset.UTC));
  }

  public ModelHttpRequest sign(ModelHttpRequest request, String region, String service,
      OffsetDateTime now);
}
