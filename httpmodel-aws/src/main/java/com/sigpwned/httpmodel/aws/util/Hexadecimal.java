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

import java.nio.charset.StandardCharsets;

public final class Hexadecimal {
  private Hexadecimal() {}

  private static final byte[] HEX_ARRAY = "0123456789abcdef".getBytes(StandardCharsets.US_ASCII);

  public static String toHexString(byte[] bytes) {
    byte[] hexChars = new byte[2 * bytes.length];
    for (int i = 0; i < bytes.length; i++) {
      int v = bytes[i] & 0xFF;
      hexChars[(2 * i) + 0] = HEX_ARRAY[(v >>> 4) & 0x0F];
      hexChars[(2 * i) + 1] = HEX_ARRAY[(v >>> 0) & 0x0F];
    }
    return new String(hexChars, StandardCharsets.ISO_8859_1);
  }
}
