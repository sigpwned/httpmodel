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
package com.sigpwned.httpmodel.host;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.junit.Test;
import com.sigpwned.httpmodel.core.model.host.ModelHttpIpV4Host;

public class ModelHttpIpV4HostTest {
  @Test
  public void shouldAcceptValidAddress() throws UnknownHostException {
    assertThat(ModelHttpIpV4Host.fromString("127.0.0.1"),
        is(ModelHttpIpV4Host.of((Inet4Address) InetAddress.getByName("127.0.0.1"))));
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotAcceptInvalidAddress() {
    ModelHttpIpV4Host.fromString("!&*#@$*");
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotAcceptValidHostname() {
    ModelHttpIpV4Host.fromString("example.com");
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotAcceptValidIpV6Address() {
    ModelHttpIpV4Host.fromString("[2001:0db8:85a3:0000:0000:8a2e:0370:7334]");
  }
}
