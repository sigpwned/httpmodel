package com.sigpwned.httpmodel.core.client.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.sigpwned.httpmodel.core.client.DefaultModelHttpClientBase;
import com.sigpwned.httpmodel.core.client.ModelHttpClient;
import com.sigpwned.httpmodel.core.client.connector.ModelHttpConnector;
import com.sigpwned.httpmodel.core.model.ModelHttpHeaders.Header;
import com.sigpwned.httpmodel.core.model.ModelHttpMediaType;
import com.sigpwned.httpmodel.core.model.ModelHttpRequest;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;
import com.sigpwned.httpmodel.core.model.ModelHttpResponse;
import com.sigpwned.httpmodel.core.model.ModelHttpResponseHead;
import com.sigpwned.httpmodel.core.util.ModelHttpHeaderNames;

/**
 * <p>
 * Performs a synchronous, round-trip HTTP request. Uses the standard set of steps as documented on
 * {@link ModelHttpBeanClient}.
 * </p>
 *
 * @see ModelHttpClient
 */
public class DefaultModelHttpBeanClient extends DefaultModelHttpClientBase
    implements ModelHttpBeanClient {
  private final List<ModelHttpBeanClientRequestMapper<?>> requestMappers;
  private final List<ModelHttpBeanClientResponseMapper<?>> responseMappers;
  private final List<ModelHttpBeanClientExceptionMapper> exceptionMappers;

  public DefaultModelHttpBeanClient(ModelHttpConnector connector) {
    super(connector);
    this.requestMappers = new ArrayList<>();
    this.responseMappers = new ArrayList<>();
    this.exceptionMappers = new ArrayList<>();
  }

  @Override
  public void registerRequestMapper(ModelHttpBeanClientRequestMapper<?> requestMapper) {
    // TODO Mapper order?
    if (requestMapper == null)
      throw new NullPointerException();
    requestMappers.add(requestMapper);
  }

  @Override
  public void registerResponseMapper(ModelHttpBeanClientResponseMapper<?> responseMapper) {
    // TODO Mapper order?
    if (responseMapper == null)
      throw new NullPointerException();
    responseMappers.add(responseMapper);
  }

  @Override
  public void registerExceptionMapper(ModelHttpBeanClientExceptionMapper exceptionMapper) {
    // TODO Mapper order?
    if (exceptionMapper == null)
      throw new NullPointerException();
    exceptionMappers.add(exceptionMapper);
  }

  @Override
  public <RequestT, ResponseT> ResponseT send(ModelHttpRequestHead httpRequestHead,
      RequestT request, Class<ResponseT> responseType) throws IOException {
    doRequestFilters(httpRequestHead);

    ModelHttpResponse httpResponse;
    try (ModelHttpRequest httpRequest = doMapRequest(httpRequestHead, request)) {
      httpRequestHead = ModelHttpRequestHead.fromRequest(httpRequest);

      doRequestInterceptors(httpRequest);

      httpResponse = doSend(httpRequest);
    }

    Optional<ResponseT> maybeResponse = null;
    try {
      ModelHttpResponseHead httpResponseHead =
          new ModelHttpResponseHead(httpResponse.getStatusCode(), httpResponse.getHeaders());

      doResponseFilters(httpResponseHead);

      doResponseInterceptors(httpResponse);

      IOException problem = doMapException(httpRequestHead, httpResponse);
      if (problem != null)
        throw problem;

      maybeResponse =
          Optional.ofNullable(doMapResponse(httpRequestHead, httpResponse, responseType));
    } finally {
      if (maybeResponse == null)
        httpResponse.close();
    }

    return maybeResponse.orElse(null);
  }

  /**
   * hook
   */
  protected <RequestT> ModelHttpRequest doMapRequest(ModelHttpRequestHead requestHead,
      RequestT request) throws IOException {
    if (request == null)
      return new ModelHttpRequest(requestHead, null);
    // TODO What if there is no mapper?
    ModelHttpMediaType contentType =
        requestHead.getHeaders().findFirstHeaderByName(ModelHttpHeaderNames.CONTENT_TYPE)
            .map(Header::getValue).map(ModelHttpMediaType::fromString).orElse(null);
    Optional<ModelHttpBeanClientRequestMapper<RequestT>> maybeRequestMapper =
        findRequestMapperForRequest(request, contentType);
    ModelHttpBeanClientRequestMapper<RequestT> requestMapper = maybeRequestMapper.get();
    return requestMapper.mapRequest(requestHead, request);
  }

  /**
   * hook
   */
  protected IOException doMapException(ModelHttpRequestHead httpRequestHead,
      ModelHttpResponse httpResponseHead) {
    for (ModelHttpBeanClientExceptionMapper exceptionMapper : getExceptionMappers()) {
      IOException problem = exceptionMapper.mapException(httpRequestHead, httpResponseHead);
      if (problem != null)
        return problem;
    }
    return null;
  }

  /**
   * hook
   */
  protected <ResponseT> ResponseT doMapResponse(ModelHttpRequestHead httpRequestHead,
      ModelHttpResponse httpResponse, Class<ResponseT> responseType) throws IOException {
    // TODO What if there is no mapper?
    ModelHttpMediaType contentType =
        httpResponse.getHeaders().findFirstHeaderByName(ModelHttpHeaderNames.CONTENT_TYPE)
            .map(Header::getValue).map(ModelHttpMediaType::fromString).orElse(null);
    Optional<ModelHttpBeanClientResponseMapper<ResponseT>> maybeResponseMapper =
        findResponseMapperForResponseType(responseType, contentType);
    ModelHttpBeanClientResponseMapper<ResponseT> responseMapper = maybeResponseMapper.get();
    return responseMapper.mapResponse(httpRequestHead, httpResponse);
  }

  /**
   * hook
   */
  protected ModelHttpResponse doSend(ModelHttpRequest request) throws IOException {
    return getConnector().send(request);
  }

  @SuppressWarnings("unchecked")
  protected <T> Optional<ModelHttpBeanClientRequestMapper<T>> findRequestMapperForRequest(T request,
      ModelHttpMediaType contentType) {
    if (request == null)
      throw new NullPointerException();
    Class<T> requestType = (Class<T>) request.getClass();
    return getRequestMappers().stream().filter(m -> m.isMappable(requestType, contentType))
        .map(m -> (ModelHttpBeanClientRequestMapper<T>) m).findFirst();
  }

  @SuppressWarnings("unchecked")
  protected <T> Optional<ModelHttpBeanClientResponseMapper<T>> findResponseMapperForResponseType(
      Class<T> responseType, ModelHttpMediaType contentType) {
    if (responseType == null)
      throw new NullPointerException();
    return getResponseMappers().stream().filter(m -> m.isMappable(responseType, contentType))
        .map(m -> (ModelHttpBeanClientResponseMapper<T>) m).findFirst();
  }

  private List<ModelHttpBeanClientRequestMapper<?>> getRequestMappers() {
    return requestMappers;
  }

  private List<ModelHttpBeanClientResponseMapper<?>> getResponseMappers() {
    return responseMappers;
  }

  private List<ModelHttpBeanClientExceptionMapper> getExceptionMappers() {
    return exceptionMappers;
  }
}
