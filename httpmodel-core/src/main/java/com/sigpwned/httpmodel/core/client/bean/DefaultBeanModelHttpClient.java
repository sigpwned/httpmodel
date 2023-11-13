package com.sigpwned.httpmodel.core.client.bean;

import static java.util.Objects.requireNonNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import com.sigpwned.httpmodel.core.BeanModelHttpClient;
import com.sigpwned.httpmodel.core.ModelHttpClient;
import com.sigpwned.httpmodel.core.model.ModelHttpHeaders.Header;
import com.sigpwned.httpmodel.core.model.ModelHttpMediaType;
import com.sigpwned.httpmodel.core.model.ModelHttpRequest;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestBuilder;
import com.sigpwned.httpmodel.core.model.ModelHttpResponse;
import com.sigpwned.httpmodel.core.util.ModelHttpHeaderNames;
import com.sigpwned.httpmodel.core.util.ModelHttpMediaTypes;

public class DefaultBeanModelHttpClient implements BeanModelHttpClient {
  private final ModelHttpClient client;
  private final List<RequestMapper<?>> requestMappers;
  private final List<ResponseMapper<?>> responseMappers;
  private final List<ExceptionMapper> exceptionMappers;

  public DefaultBeanModelHttpClient(ModelHttpClient client) {
    this.client = requireNonNull(client);
    this.requestMappers = new ArrayList<>();
    this.responseMappers = new ArrayList<>();
    this.exceptionMappers = new ArrayList<>();
  }

  public void registerRequestMapper(RequestMapper<?> requestMapper) {
    // TODO Mapper order?
    if (requestMapper == null)
      throw new NullPointerException();
    requestMappers.add(requestMapper);
  }

  public void registerResponseMapper(ResponseMapper<?> responseMapper) {
    // TODO Mapper order?
    if (responseMapper == null)
      throw new NullPointerException();
    responseMappers.add(responseMapper);
  }

  public void registerExceptionMapper(ExceptionMapper exceptionMapper) {
    // TODO Mapper order?
    if (exceptionMapper == null)
      throw new NullPointerException();
    exceptionMappers.add(exceptionMapper);
  }

  @Override
  public <RequestT, ResponseT> ResponseT send(ModelHttpRequestBuilder httpRequestBuilder,
      RequestT request, Class<ResponseT> responseType) throws IOException {
    if (request != null) {
      // TODO What if there is no mapper?
      Optional<RequestMapper<RequestT>> maybeRequestMapper = findRequestMapperForRequest(request);
      RequestMapper<RequestT> requestMapper = maybeRequestMapper.get();
      requestMapper.mapRequest(httpRequestBuilder, request);
    }

    try (ModelHttpRequest rawHttpRequest = httpRequestBuilder.build();
        ModelHttpRequest filteredHttpRequest = filterRequest(rawHttpRequest);
        ModelHttpResponse rawHttpResponse = getClient().send(filteredHttpRequest);
        ModelHttpResponse filteredHttpResponse = filterResponse(rawHttpResponse)) {

      IOException mappedException =
          getExceptionMappers().stream().map(m -> m.mapException(filteredHttpResponse))
              .filter(Objects::nonNull).findFirst().orElse(null);
      if (mappedException != null)
        throw mappedException;

      if (!filteredHttpResponse.getEntity().isPresent())
        return null;

      ModelHttpMediaType responseMediaType = filteredHttpResponse.getHeaders()
          .findFirstHeaderByName(ModelHttpHeaderNames.CONTENT_TYPE).map(Header::getValue)
          .map(ModelHttpMediaType::fromString).orElse(ModelHttpMediaTypes.APPLICATION_OCTET_STREAM);

      // TODO What if there is no mapper?
      Optional<ResponseMapper<ResponseT>> maybeResponseMapper =
          findResponseMapperForResponseType(responseType, responseMediaType);
      ResponseMapper<ResponseT> responseMapper = maybeResponseMapper.get();
      ResponseT response = responseMapper.mapResponse(filteredHttpRequest, filteredHttpResponse);

      return response;
    }
  }

  @Override
  public void close() throws IOException {
    getClient().close();
  }

  /**
   * hook
   */
  protected ModelHttpRequest filterRequest(ModelHttpRequest request) {
    return request;
  }

  /**
   * hook
   */
  protected ModelHttpResponse filterResponse(ModelHttpResponse response) {
    return response;
  }

  @SuppressWarnings("unchecked")
  protected <T> Optional<RequestMapper<T>> findRequestMapperForRequest(T request) {
    if (request == null)
      throw new NullPointerException();
    Class<T> requestType = (Class<T>) request.getClass();
    return getRequestMappers().stream().filter(m -> m.isMappable(requestType))
        .map(m -> (RequestMapper<T>) m).findFirst();
  }

  @SuppressWarnings("unchecked")
  protected <T> Optional<ResponseMapper<T>> findResponseMapperForResponseType(Class<T> responseType,
      ModelHttpMediaType contentType) {
    if (responseType == null)
      throw new NullPointerException();
    return getResponseMappers().stream().filter(m -> m.isMappable(responseType, contentType))
        .map(m -> (ResponseMapper<T>) m).findFirst();
  }

  private ModelHttpClient getClient() {
    return client;
  }

  private List<RequestMapper<?>> getRequestMappers() {
    return requestMappers;
  }

  private List<ResponseMapper<?>> getResponseMappers() {
    return responseMappers;
  }

  private List<ExceptionMapper> getExceptionMappers() {
    return exceptionMappers;
  }
}
