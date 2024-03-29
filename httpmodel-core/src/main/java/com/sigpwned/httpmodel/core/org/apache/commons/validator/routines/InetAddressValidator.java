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
package com.sigpwned.httpmodel.core.org.apache.commons.validator.routines;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

/**
 * Pulled from Apache Commons Validator project. Original license appears above.
 */
public class InetAddressValidator {
  private static final int MAX_BYTE = 128;

  private static final int IPV4_MAX_OCTET_VALUE = 255;

  private static final int MAX_UNSIGNED_SHORT = 0xffff;

  private static final int BASE_16 = 16;

  private static final Pattern IPV4_REGEX =
      Pattern.compile("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$");

  // Max number of hex groups (separated by :) in an IPV6 address
  private static final int IPV6_MAX_HEX_GROUPS = 8;

  // Max hex digits in each IPv6 group
  private static final int IPV6_MAX_HEX_DIGITS_PER_GROUP = 4;

  /**
   * Singleton instance of this class.
   */
  private static final InetAddressValidator VALIDATOR = new InetAddressValidator();

  /**
   * Returns the singleton instance of this validator.
   * 
   * @return the singleton instance of this validator
   */
  public static InetAddressValidator getInstance() {
    return VALIDATOR;
  }

  /**
   * Checks if the specified string is a valid IP address.
   * 
   * @param inetAddress the string to validate
   * @return true if the string validates as an IP address
   */
  public boolean isValid(String inetAddress) {
    return isValidInet4Address(inetAddress) || isValidInet6Address(inetAddress);
  }

  /**
   * Validates an IPv4 address. Returns true if valid.
   * 
   * @param inet4Address the IPv4 address to validate
   * @return true if the argument contains a valid IPv4 address
   */
  public boolean isValidInet4Address(String inet4Address) {
    Matcher m = IPV4_REGEX.matcher(inet4Address);
    if (!m.matches())
      return false;

    // verify that address conforms to generic IPv4 format
    String[] groups = new String[] {m.group(1), m.group(2), m.group(3), m.group(4)};

    // verify that address subgroups are legal
    for (String ipSegment : groups) {
      if (ipSegment == null || ipSegment.isEmpty()) {
        return false;
      }

      int iIpSegment = 0;

      try {
        iIpSegment = Integer.parseInt(ipSegment);
      } catch (NumberFormatException e) {
        return false;
      }

      if (iIpSegment > IPV4_MAX_OCTET_VALUE) {
        return false;
      }

      if (ipSegment.length() > 1 && ipSegment.startsWith("0")) {
        return false;
      }

    }

    return true;
  }

  /**
   * Validates an IPv6 address. Returns true if valid.
   * 
   * @param inet6Address the IPv6 address to validate
   * @return true if the argument contains a valid IPv6 address
   *
   * @since 1.4.1
   */
  public boolean isValidInet6Address(String inet6Address) {
    String[] parts;
    // remove prefix size. This will appear after the zone id (if any)
    parts = inet6Address.split("/", -1);
    if (parts.length > 2) {
      return false; // can only have one prefix specifier
    }
    if (parts.length == 2) {
      if (!parts[1].matches("\\d{1,3}")) {
        return false; // not a valid number
      }
      int bits = Integer.parseInt(parts[1]); // cannot fail because of RE check
      if (bits < 0 || bits > MAX_BYTE) {
        return false; // out of range
      }
    }
    // remove zone-id
    parts = parts[0].split("%", -1);
    if (parts.length > 2) {
      return false;
    }
    // The id syntax is implementation independent, but it presumably cannot allow:
    // whitespace, '/' or '%'
    if ((parts.length == 2) && !parts[1].matches("[^\\s/%]+")) {
      return false; // invalid id
    }
    inet6Address = parts[0];
    boolean containsCompressedZeroes = inet6Address.contains("::");
    if (containsCompressedZeroes
        && (inet6Address.indexOf("::") != inet6Address.lastIndexOf("::"))) {
      return false;
    }
    if ((inet6Address.startsWith(":") && !inet6Address.startsWith("::"))
        || (inet6Address.endsWith(":") && !inet6Address.endsWith("::"))) {
      return false;
    }
    String[] octets = inet6Address.split(":");
    if (containsCompressedZeroes) {
      List<String> octetList = new ArrayList<>(Arrays.asList(octets));
      if (inet6Address.endsWith("::")) {
        // String.split() drops ending empty segments
        octetList.add("");
      } else if (inet6Address.startsWith("::") && !octetList.isEmpty()) {
        octetList.remove(0);
      }
      octets = octetList.toArray(new String[octetList.size()]);
    }
    if (octets.length > IPV6_MAX_HEX_GROUPS) {
      return false;
    }
    int validOctets = 0;
    int emptyOctets = 0; // consecutive empty chunks
    for (int index = 0; index < octets.length; index++) {
      String octet = octets[index];
      if (octet.isEmpty()) {
        emptyOctets++;
        if (emptyOctets > 1) {
          return false;
        }
      } else {
        emptyOctets = 0;
        // Is last chunk an IPv4 address?
        if (index == octets.length - 1 && octet.contains(".")) {
          if (!isValidInet4Address(octet)) {
            return false;
          }
          validOctets += 2;
          continue;
        }
        if (octet.length() > IPV6_MAX_HEX_DIGITS_PER_GROUP) {
          return false;
        }
        int octetInt = 0;
        try {
          octetInt = Integer.parseInt(octet, BASE_16);
        } catch (NumberFormatException e) {
          return false;
        }
        if (octetInt < 0 || octetInt > MAX_UNSIGNED_SHORT) {
          return false;
        }
      }
      validOctets++;
    }
    if (validOctets > IPV6_MAX_HEX_GROUPS
        || (validOctets < IPV6_MAX_HEX_GROUPS && !containsCompressedZeroes)) {
      return false;
    }
    return true;
  }
}
