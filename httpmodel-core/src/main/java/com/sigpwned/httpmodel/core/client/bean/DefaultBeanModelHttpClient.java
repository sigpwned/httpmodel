package com.sigpwned.httpmodel.core.client.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.sigpwned.httpmodel.core.BeanModelHttpClient;
import com.sigpwned.httpmodel.core.client.DefaultModelHttpClientBase;
import com.sigpwned.httpmodel.core.client.ModelHttpConnector;
import com.sigpwned.httpmodel.core.model.ModelHttpHeaders.Header;
import com.sigpwned.httpmodel.core.model.ModelHttpMediaType;
import com.sigpwned.httpmodel.core.model.ModelHttpRequest;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;
import com.sigpwned.httpmodel.core.model.ModelHttpResponse;
import com.sigpwned.httpmodel.core.model.ModelHttpResponseHead;
import com.sigpwned.httpmodel.core.util.ModelHttpHeaderNames;

public class DefaultBeanModelHttpClient extends DefaultModelHttpClientBase
    implements BeanModelHttpClient {
  private final List<ModelHttpClientRequestMapper<?>> requestMappers;
  private final List<ModelHttpClientResponseMapper<?>> responseMappers;
  private final List<ModelHttpClientExceptionMapper> exceptionMappers;

  public DefaultBeanModelHttpClient(ModelHttpConnector connector) {
    super(connector);
    this.requestMappers = new ArrayList<>();
    this.responseMappers = new ArrayList<>();
    this.exceptionMappers = new ArrayList<>();
  }

  public void registerRequestMapper(ModelHttpClientRequestMapper<?> requestMapper) {
    // TODO Mapper order?
    if (requestMapper == null)
      throw new NullPointerException();
    requestMappers.add(requestMapper);
  }

  public void registerResponseMapper(ModelHttpClientResponseMapper<?> responseMapper) {
    // TODO Mapper order?
    if (responseMapper == null)
      throw new NullPointerException();
    responseMappers.add(responseMapper);
  }

  public void registerExceptionMapper(ModelHttpClientExceptionMapper exceptionMapper) {
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
    Optional<ModelHttpClientRequestMapper<RequestT>> maybeRequestMapper =
        findRequestMapperForRequest(request, contentType);
    ModelHttpClientRequestMapper<RequestT> requestMapper = maybeRequestMapper.get();
    return requestMapper.mapRequest(requestHead, request);
  }

  /**
   * hook
   */
  protected IOException doMapException(ModelHttpRequestHead httpRequestHead,
      ModelHttpResponse httpResponseHead) {
    for (ModelHttpClientExceptionMapper exceptionMapper : getExceptionMappers()) {
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
    Optional<ModelHttpClientResponseMapper<ResponseT>> maybeResponseMapper =
        findResponseMapperForResponseType(responseType, contentType);
    ModelHttpClientResponseMapper<ResponseT> responseMapper = maybeResponseMapper.get();
    return responseMapper.mapResponse(httpRequestHead, httpResponse);
  }

  /**
   * hook
   *
   * @throws IOException
   */
  protected ModelHttpResponse doSend(ModelHttpRequest request) throws IOException {
    return getConnector().send(request);
  }

  @SuppressWarnings("unchecked")
  protected <T> Optional<ModelHttpClientRequestMapper<T>> findRequestMapperForRequest(T request,
      ModelHttpMediaType contentType) {
    if (request == null)
      throw new NullPointerException();
    Class<T> requestType = (Class<T>) request.getClass();
    return getRequestMappers().stream().filter(m -> m.isMappable(requestType, contentType))
        .map(m -> (ModelHttpClientRequestMapper<T>) m).findFirst();
  }

  @SuppressWarnings("unchecked")
  protected <T> Optional<ModelHttpClientResponseMapper<T>> findResponseMapperForResponseType(
      Class<T> responseType, ModelHttpMediaType contentType) {
    if (responseType == null)
      throw new NullPointerException();
    return getResponseMappers().stream().filter(m -> m.isMappable(responseType, contentType))
        .map(m -> (ModelHttpClientResponseMapper<T>) m).findFirst();
  }

  private List<ModelHttpClientRequestMapper<?>> getRequestMappers() {
    return requestMappers;
  }

  private List<ModelHttpClientResponseMapper<?>> getResponseMappers() {
    return responseMappers;
  }

  private List<ModelHttpClientExceptionMapper> getExceptionMappers() {
    return exceptionMappers;
  }
}
