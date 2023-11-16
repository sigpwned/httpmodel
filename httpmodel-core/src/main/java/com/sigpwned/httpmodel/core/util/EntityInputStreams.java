/*-
 * =================================LICENSE_START==================================
 * httpmodel-core
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
package com.sigpwned.httpmodel.core.util;

import java.io.IOException;
import java.util.Arrays;
import com.sigpwned.httpmodel.core.io.EntityInputStream;
import com.sigpwned.httpmodel.core.io.InputStreamBufferingStrategy;

public final class EntityInputStreams {
  private EntityInputStreams() {}

  public static int hashCode(EntityInputStream in) throws IOException {
    return hashCode(in, InputStreamBufferingStrategy.DEFAULT);
  }

  public static int hashCode(EntityInputStream in, InputStreamBufferingStrategy bufferingStrategy)
      throws IOException {
    in.buffer(bufferingStrategy);
    byte[] entityHashCode = MoreHashing.sha1(in);
    in.restart();
    return Arrays.hashCode(entityHashCode);
  }

  private static final int BUFSIZE = 8 * 1024;

  public static boolean contentEquals(EntityInputStream a, EntityInputStream b) throws IOException {
    a.buffer(InputStreamBufferingStrategy.DEFAULT);
    b.buffer(InputStreamBufferingStrategy.DEFAULT);
    try {
      byte[] abuf = new byte[BUFSIZE];
      byte[] bbuf = new byte[BUFSIZE];
      do {
        int anread = MoreByteStreams.read(a, abuf);
        int bnread = MoreByteStreams.read(b, bbuf);
        if (anread != bnread)
          return false;
        if (!Arrays.equals(abuf, bbuf))
          return false;
        if (anread < abuf.length || bnread < bbuf.length)
          return true;
      } while (true);
    } finally {
      a.restart();
      b.restart();
    }
  }
}
