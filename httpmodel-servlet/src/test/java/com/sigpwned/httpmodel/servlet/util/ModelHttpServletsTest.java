package com.sigpwned.httpmodel.servlet.util;

import static java.util.Collections.emptyEnumeration;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Test;
import com.sigpwned.httpmodel.ModelHttpEntity;
import com.sigpwned.httpmodel.ModelHttpRequest;
import com.sigpwned.httpmodel.ModelHttpResponse;
import com.sigpwned.httpmodel.ModelHttpUrl;
import com.sigpwned.httpmodel.util.ModelHttpMediaTypes;
import com.sigpwned.httpmodel.util.ModelHttpMethods;
import com.sigpwned.httpmodel.util.ModelHttpStatusCodes;
import com.sigpwned.httpmodel.util.ModelHttpVersions;

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

    ModelHttpRequest request = ModelHttpServlets.toModelRequest(req);

    assertThat(request, is(ModelHttpRequest.of(ModelHttpVersions.HTTP_1_1, ModelHttpMethods.GET,
        ModelHttpUrl.fromString("http://localhost:8080/path/to/example"), Optional.empty())));
  }

  @Test
  public void shouldConvertResponseProperly() throws IOException {
    byte[] bytes = "hello".getBytes(StandardCharsets.UTF_8);

    ModelHttpResponse response = ModelHttpResponse.of(ModelHttpStatusCodes.OK,
        ModelHttpEntity.of(ModelHttpMediaTypes.TEXT_PLAIN, bytes));

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

    ModelHttpServlets.fromModelResponse(res, response);

    verify(res).setContentType(ModelHttpMediaTypes.TEXT_PLAIN.toString());
    verify(res).setContentLength(bytes.length);
    verify(res).setStatus(ModelHttpStatusCodes.OK);

    assertThat(out.toByteArray(), is(bytes));
  }
}
