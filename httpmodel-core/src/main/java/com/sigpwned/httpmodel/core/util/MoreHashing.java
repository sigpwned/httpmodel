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
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class MoreHashing {
  private MoreHashing() {}

  /**
   * https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#MessageDigest
   */
  public static final String SHA1 = "SHA-1";

  /**
   * https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#MessageDigest
   */
  public static final String SHA256 = "SHA-256";

  /**
   * https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#MessageDigest
   */
  public static final String MD5 = "MD5";

  public static byte[] sha1(InputStream in) throws IOException {
    MessageDigest d;
    try {
      d = MessageDigest.getInstance(SHA1);
    } catch (NoSuchAlgorithmException e) {
      throw new AssertionError("According to spec, JVM must support " + SHA1, e);
    }
    return hash(d, in);
  }

  public static byte[] sha256(InputStream in) throws IOException {
    MessageDigest d;
    try {
      d = MessageDigest.getInstance(SHA256);
    } catch (NoSuchAlgorithmException e) {
      throw new AssertionError("According to spec, JVM must support " + SHA256, e);
    }
    return hash(d, in);
  }

  public static byte[] md5(InputStream in) throws IOException {
    MessageDigest d;
    try {
      d = MessageDigest.getInstance(MD5);
    } catch (NoSuchAlgorithmException e) {
      throw new AssertionError("According to spec, JVM must support " + MD5, e);
    }
    return hash(d, in);
  }

  public static byte[] hash(MessageDigest d, InputStream in) throws IOException {
    byte[] buf = new byte[16384];
    for (int nread = in.read(buf); nread != -1; nread = in.read(buf)) {
      d.update(buf, 0, nread);
    }
    return d.digest();
  }
}
