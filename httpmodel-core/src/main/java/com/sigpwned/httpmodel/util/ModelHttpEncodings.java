package com.sigpwned.httpmodel.util;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

public final class ModelHttpEncodings {
  private ModelHttpEncodings() {}

  /**
   * URLEncode the given string. The characters 0-9, A-Z, a-z, "-", "_", ".", and "~" all stay the
   * same. All other characters are percent encoded.
   * 
   * @see <a href=
   *      "https://en.wikipedia.org/wiki/Percent-encoding">https://en.wikipedia.org/wiki/Percent-encoding</a>
   */
  public static String urlencode(String s) {
    byte[] bs = s.getBytes(StandardCharsets.UTF_8);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    for (int i = 0; i < bs.length; i++) {
      byte b = bs[i];
      if ((b >= '0' && b <= '9') || (b >= 'A' && b <= 'Z') || (b >= 'a' && b <= 'z') || b == '-'
          || b == '.' || b == '_' || b == '~') {
        out.write(b);
      } else {
        out.write('%');
        out.write(urlencode((b & 0xF0) >>> 4));
        out.write(urlencode(b & 0x0F));
      }
    }

    return new String(out.toByteArray(), StandardCharsets.US_ASCII);
  }

  private static byte urlencode(int b) {
    if (b >= 0 && b <= 9) {
      return (byte) ('0' + (b - 0));
    } else if (b >= 10 && b <= 15) {
      return (byte) ('A' + (b - 10));
    } else {
      throw new IllegalArgumentException("value not between 0 and 15");
    }
  }

  /**
   * URLDecode the given string. Percent encoded sequences are decoded, other characters are just
   * passed through.
   * 
   * @see <a href=
   *      "https://en.wikipedia.org/wiki/Percent-encoding">https://en.wikipedia.org/wiki/Percent-encoding</a>
   */
  public static String urldecode(String s) {
    byte[] bs = s.getBytes(StandardCharsets.US_ASCII);

    ByteArrayOutputStream out = new ByteArrayOutputStream();

    int index = 0;
    while (index < bs.length) {
      byte b = bs[index++];
      if (b == '%') {
        if (index + 2 > bs.length)
          throw new IllegalArgumentException("incomplete percent encoding");
        byte upper = urldecode(bs[index++]);
        byte lower = urldecode(bs[index++]);
        out.write((upper << 4) | (lower << 0));
      } else {
        out.write(b);
      }
    }

    return new String(out.toByteArray(), StandardCharsets.UTF_8);
  }

  private static byte urldecode(int b) {
    if (b >= '0' && b <= '9') {
      return (byte) (b - '0');
    } else if (b >= 'A' && b <= 'F') {
      return (byte) (b - 'A' + 10);
    } else if (b >= 'a' && b <= 'f') {
      return (byte) (b - 'a' + 10);
    } else {
      throw new IllegalArgumentException("invalid hex value");
    }
  }
}
