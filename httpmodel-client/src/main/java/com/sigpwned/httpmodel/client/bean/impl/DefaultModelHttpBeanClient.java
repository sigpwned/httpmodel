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
package com.sigpwned.httpmodel.client.bean.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import com.sigpwned.httpmodel.client.ModelHttpClient;
import com.sigpwned.httpmodel.client.ModelHttpConnector;
import com.sigpwned.httpmodel.client.bean.ModelHttpBeanClient;
import com.sigpwned.httpmodel.client.bean.ModelHttpBeanClientExceptionMapper;
import com.sigpwned.httpmodel.client.bean.ModelHttpBeanClientRequestFilter;
import com.sigpwned.httpmodel.client.bean.ModelHttpBeanClientRequestMapper;
import com.sigpwned.httpmodel.client.bean.ModelHttpBeanClientResponseFilter;
import com.sigpwned.httpmodel.client.bean.ModelHttpBeanClientResponseMapper;
import com.sigpwned.httpmodel.client.connector.UrlConnectionModelHttpConnector;
import com.sigpwned.httpmodel.client.impl.DefaultModelHttpClientBase;
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
  private final List<ModelHttpBeanClientRequestFilter> beanRequestFilters;
  private final List<ModelHttpBeanClientResponseFilter> beanResponseFilters;
  private final List<ModelHttpBeanClientRequestMapper<?>> requestMappers;
  private final List<ModelHttpBeanClientResponseMapper<?>> responseMappers;
  private final List<ModelHttpBeanClientExceptionMapper> exceptionMappers;

  public DefaultModelHttpBeanClient() {
    this(new UrlConnectionModelHttpConnector());
  }

  public DefaultModelHttpBeanClient(ModelHttpConnector connector) {
    super(connector);
    this.beanRequestFilters = new ArrayList<>();
    this.beanResponseFilters = new ArrayList<>();
    this.requestMappers = new ArrayList<>();
    this.responseMappers = new ArrayList<>();
    this.exceptionMappers = new ArrayList<>();
  }

  private static final Comparator<ModelHttpBeanClientRequestFilter> BEAN_CLIENT_REQUEST_FILTER_COMPARATOR =
      Comparator.comparingInt(ModelHttpBeanClientRequestFilter::priority);

  @Override
  public void addBeanRequestFilter(ModelHttpBeanClientRequestFilter beanRequestFilter) {
    if (beanRequestFilter == null)
      throw new NullPointerException();
    beanRequestFilters.add(beanRequestFilter);
    beanRequestFilters.sort(BEAN_CLIENT_REQUEST_FILTER_COMPARATOR);
  }

  @Override
  public void removeBeanRequestFilter(ModelHttpBeanClientRequestFilter beanRequestFilter) {
    beanRequestFilters.remove(beanRequestFilter);
  }

  private static final Comparator<ModelHttpBeanClientResponseFilter> BEAN_CLIENT_RESPONSE_FILTER_COMPARATOR =
      Comparator.comparingInt(ModelHttpBeanClientResponseFilter::priority);

  @Override
  public void addBeanResponseFilter(ModelHttpBeanClientResponseFilter beanResponseFilter) {
    if (beanResponseFilter == null)
      throw new NullPointerException();
    beanResponseFilters.add(beanResponseFilter);
    beanResponseFilters.sort(BEAN_CLIENT_RESPONSE_FILTER_COMPARATOR);
  }

  @Override
  public void removeBeanResponseFilter(ModelHttpBeanClientResponseFilter beanResponseFilter) {
    beanResponseFilters.remove(beanResponseFilter);
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
      RequestT beanRequest, Class<ResponseT> beanResponseType) throws IOException {
    doRequestFilters(httpRequestHead);

    doBeanRequestFilters(httpRequestHead, beanRequest);

    ModelHttpResponse httpResponse;
    try (ModelHttpRequest httpRequest = doMapRequest(httpRequestHead, beanRequest)) {
      doRequestInterceptors(httpRequest);

      httpRequestHead = ModelHttpRequestHead.fromRequest(httpRequest);

      httpResponse = doSend(httpRequest);
    }

    Optional<ResponseT> maybeBeanResponse = null;
    try {
      ModelHttpResponseHead httpResponseHead =
          new ModelHttpResponseHead(httpResponse.getStatusCode(), httpResponse.getHeaders());

      doResponseFilters(httpResponseHead);

      doResponseInterceptors(httpResponse);

      Exception problem = doMapException(httpRequestHead, httpResponse);
      if (problem != null) {
        if (problem instanceof RuntimeException) {
          throw (RuntimeException) problem;
        } else if (problem instanceof IOException) {
          throw (IOException) problem;
        } else {
          throw new IOException(problem);
        }
      }

      maybeBeanResponse =
          Optional.ofNullable(doMapResponse(httpRequestHead, httpResponse, beanResponseType));

      httpResponseHead = ModelHttpResponseHead.fromResponse(httpResponse);

      doBeanResponseFilters(httpRequestHead, beanRequest, httpResponseHead,
          maybeBeanResponse.orElse(null));
    } finally {
      if (maybeBeanResponse == null)
        httpResponse.close();
    }

    return maybeBeanResponse.orElse(null);
  }

  /**
   * hook
   */
  protected void doBeanRequestFilters(ModelHttpRequestHead httpRequestHead, Object requestBean) {
    for (ModelHttpBeanClientRequestFilter filter : getBeanRequestFilters())
      filter.filter(httpRequestHead, requestBean);
  }

  /**
   * hook
   */
  protected void doBeanResponseFilters(ModelHttpRequestHead httpRequestHead, Object requestBean,
      ModelHttpResponseHead httpResponseHead, Object responseBean) {
    for (ModelHttpBeanClientResponseFilter filter : getBeanResponseFilters())
      filter.filter(httpRequestHead, requestBean, httpResponseHead, responseBean);
  }

  /**
   * hook
   */
  protected <RequestT> ModelHttpRequest doMapRequest(ModelHttpRequestHead httpRequestHead,
      RequestT requestBean) throws IOException {
    if (requestBean == null)
      return new ModelHttpRequest(httpRequestHead);
    // TODO What if there is no mapper?
    ModelHttpMediaType contentType =
        httpRequestHead.getHeaders().findFirstHeaderByName(ModelHttpHeaderNames.CONTENT_TYPE)
            .map(Header::getValue).map(ModelHttpMediaType::fromString).orElse(null);
    Optional<ModelHttpBeanClientRequestMapper<RequestT>> maybeRequestMapper =
        findRequestMapperForRequest(requestBean, contentType);
    ModelHttpBeanClientRequestMapper<RequestT> requestMapper = maybeRequestMapper.get();
    return requestMapper.mapRequest(httpRequestHead, requestBean);
  }

  /**
   * hook
   *
   * @throws IOException
   */
  protected Exception doMapException(ModelHttpRequestHead httpRequestHead,
      ModelHttpResponse httpResponseHead) throws IOException {
    for (ModelHttpBeanClientExceptionMapper exceptionMapper : getExceptionMappers()) {
      Exception problem = exceptionMapper.mapException(httpRequestHead, httpResponseHead);
      if (problem != null) {
        return problem;
      }
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
  protected ModelHttpResponse doSend(ModelHttpRequest httpRequest) throws IOException {
    return getConnector().send(httpRequest);
  }

  @SuppressWarnings("unchecked")
  protected <T> Optional<ModelHttpBeanClientRequestMapper<T>> findRequestMapperForRequest(
      T requestBean, ModelHttpMediaType contentType) {
    if (requestBean == null)
      throw new NullPointerException();
    Class<T> requestType = (Class<T>) requestBean.getClass();
    return getRequestMappers().stream().filter(m -> m.isMappable(requestType, contentType))
        .map(m -> (ModelHttpBeanClientRequestMapper<T>) m).findFirst();
  }

  @SuppressWarnings("unchecked")
  protected <T> Optional<ModelHttpBeanClientResponseMapper<T>> findResponseMapperForResponseType(
      Class<T> responseBeanType, ModelHttpMediaType contentType) {
    if (responseBeanType == null)
      throw new NullPointerException();
    return getResponseMappers().stream().filter(m -> m.isMappable(responseBeanType, contentType))
        .map(m -> (ModelHttpBeanClientResponseMapper<T>) m).findFirst();
  }

  private List<ModelHttpBeanClientRequestFilter> getBeanRequestFilters() {
    return beanRequestFilters;
  }

  private List<ModelHttpBeanClientResponseFilter> getBeanResponseFilters() {
    return beanResponseFilters;
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
