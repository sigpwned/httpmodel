/*-
 * =================================LICENSE_START==================================
 * httpmodel-servlet
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
package com.sigpwned.httpmodel.servlet.jakarta.util;

import static java.util.Collections.emptyEnumeration;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.Test;
import com.sigpwned.httpmodel.core.entity.PlainTextModelHttpEntity;
import com.sigpwned.httpmodel.core.model.ModelHttpHeaders;
import com.sigpwned.httpmodel.core.model.ModelHttpRequest;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;
import com.sigpwned.httpmodel.core.model.ModelHttpResponse;
import com.sigpwned.httpmodel.core.model.ModelHttpUrl;
import com.sigpwned.httpmodel.core.util.ModelHttpMediaTypes;
import com.sigpwned.httpmodel.core.util.ModelHttpMethods;
import com.sigpwned.httpmodel.core.util.ModelHttpRequests;
import com.sigpwned.httpmodel.core.util.ModelHttpStatusCodes;
import com.sigpwned.httpmodel.core.util.ModelHttpVersions;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ModelHttpServletsTest {
  @Test
  public void shouldConvertGetRequestProperly() throws IOException {
    final HttpServletRequest req = mock(HttpServletRequest.class);
    when(req.getScheme()).thenReturn("http");
    when(req.getProtocol()).thenReturn("HTTP/1.1");
    when(req.getMethod()).thenReturn("GET");
    when(req.getServerName()).thenReturn("localhost");
    when(req.getServerPort()).thenReturn(8080);
    when(req.getRequestURI()).thenReturn("/path/to/example");
    when(req.getHeaderNames()).thenReturn(emptyEnumeration());

    ModelHttpRequest got = ModelHttpServlets.fromRequest(req);

    ModelHttpRequest want = new ModelHttpRequestHead(ModelHttpVersions.HTTP_1_1,
        ModelHttpMethods.GET, ModelHttpUrl.fromString("http://localhost:8080/path/to/example"),
        new ModelHttpHeaders()).toRequest();

    if (!ModelHttpRequests.equals(want, got))
      assertThat(want, is(got));
  }

  @Test
  public void shouldConvertResponseProperly() throws IOException {
    String textString = "hello";
    byte[] textBytes = textString.getBytes(StandardCharsets.UTF_8);

    ModelHttpResponse response = new ModelHttpResponse(ModelHttpStatusCodes.OK,
        new ModelHttpHeaders(), new PlainTextModelHttpEntity("hello"));

    final ByteArrayOutputStream out = new ByteArrayOutputStream();

    final HttpServletResponse res = mock(HttpServletResponse.class);
    when(res.getOutputStream()).thenReturn(new ServletOutputStream() {
      @Override
      public boolean isReady() {
        return true;
      }

      @Override
      public void setWriteListener(WriteListener writeListener) {
        // NOP
      }

      @Override
      public void write(int b) throws IOException {
        out.write(b);
      }
    });

    ModelHttpServlets.toResponse(res, response);

    verify(res).setContentType(
        ModelHttpMediaTypes.TEXT_PLAIN.withCharset(StandardCharsets.UTF_8).toString());
    verify(res).setContentLengthLong(textBytes.length);
    verify(res).setStatus(ModelHttpStatusCodes.OK);

    assertThat(out.toByteArray(), is(textBytes));
  }
}
