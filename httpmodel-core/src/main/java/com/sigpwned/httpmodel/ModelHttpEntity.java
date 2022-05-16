package com.sigpwned.httpmodel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class ModelHttpEntity {
  public static ModelHttpEntity of(ModelHttpMediaType type, byte[] data) {
    return new ModelHttpEntity(type, data);
  }

  private final ModelHttpMediaType type;

  private final byte[] data;

  public ModelHttpEntity(ModelHttpMediaType type, byte[] data) {
    if (data == null)
      throw new NullPointerException();
    this.type = type;
    this.data = data;
  }

  /**
   * @return the type
   */
  public ModelHttpMediaType getType() {
    return type;
  }

  /**
   * @return the data
   */
  private byte[] getData() {
    return data;
  }

  public InputStream readBytes() {
    return new ByteArrayInputStream(getData());
  }

  public byte[] toByteArray() {
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try {
      byte[] buf = new byte[4096];
      try (InputStream in = readBytes()) {
        for (int nread = in.read(buf); nread != -1; nread = in.read(buf)) {
          out.write(buf, 0, nread);
        }
      }
    } catch (IOException e) {
      // This should never happen, since it's all in memory.
      throw new UncheckedIOException(e);
    }

    return out.toByteArray();
  }

  public Reader readChars(Charset defaultCharset) {
    return new InputStreamReader(readBytes(), Optional.ofNullable(getType())
        .flatMap(ModelHttpMediaType::getCharset).orElse(defaultCharset));
  }

  public String toString(Charset defaultCharset) {
    StringWriter out = new StringWriter();

    try {
      char[] buf = new char[4096];
      try (Reader in = readChars(defaultCharset)) {
        for (int nread = in.read(buf); nread != -1; nread = in.read(buf)) {
          out.write(buf, 0, nread);
        }
      }
    } catch (IOException e) {
      // This should never happen, since it's all in memory.
      throw new UncheckedIOException(e);
    }

    return out.toString();
  }

  public int length() {
    return getData().length;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Arrays.hashCode(data);
    result = prime * result + Objects.hash(type);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ModelHttpEntity other = (ModelHttpEntity) obj;
    return Arrays.equals(data, other.data) && Objects.equals(type, other.type);
  }

  @Override
  public String toString() {
    return "ModelHttpEntity [type=" + type + ", data=" + Arrays.toString(data) + "]";
  }
}
