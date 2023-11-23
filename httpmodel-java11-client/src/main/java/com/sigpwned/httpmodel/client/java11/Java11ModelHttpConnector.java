package com.sigpwned.httpmodel.client.java11;

import static java.util.Objects.requireNonNull;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import com.sigpwned.httpmodel.client.java11.util.Java11ModelHttpClients;
import com.sigpwned.httpmodel.core.client.connector.ModelHttpConnector;
import com.sigpwned.httpmodel.core.model.ModelHttpRequest;
import com.sigpwned.httpmodel.core.model.ModelHttpResponse;

public class Java11ModelHttpConnector implements ModelHttpConnector {
  public static HttpClient defaultClient() {
    return HttpClient.newHttpClient();
  }

  private final HttpClient client;

  public Java11ModelHttpConnector() {
    this(defaultClient());
  }

  public Java11ModelHttpConnector(HttpClient client) {
    this.client = requireNonNull(client);
  }

  @Override
  public ModelHttpResponse send(ModelHttpRequest modelRequest) throws IOException {
    HttpRequest java11Request = Java11ModelHttpClients.toRequest(modelRequest);

    HttpResponse<InputStream> java11Response;
    try {
      java11Response = getClient().send(java11Request, BodyHandlers.ofInputStream());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new InterruptedIOException("interrupted");
    }

    ModelHttpResponse modelResponse = transformResponse(java11Response);

    return modelResponse;

  }

  /**
   * hook
   */
  protected HttpRequest transformRequest(ModelHttpRequest modelRequest) throws IOException {
    return Java11ModelHttpClients.toRequest(modelRequest);
  }

  /**
   * hook
   */
  protected ModelHttpResponse transformResponse(HttpResponse<InputStream> java11Response)
      throws IOException {
    return Java11ModelHttpClients.fromResponse(java11Response);
  }

  private HttpClient getClient() {
    return client;
  }
}
