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
package com.sigpwned.httpmodel.core.client;



import static java.util.Collections.unmodifiableList;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import com.sigpwned.httpmodel.core.ModelHttpRequestFilter;
import com.sigpwned.httpmodel.core.ModelHttpRequestInterceptor;
import com.sigpwned.httpmodel.core.ModelHttpResponseFilter;
import com.sigpwned.httpmodel.core.ModelHttpResponseInterceptor;
import com.sigpwned.httpmodel.core.client.connector.ModelHttpConnector;
import com.sigpwned.httpmodel.core.model.ModelHttpRequest;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;
import com.sigpwned.httpmodel.core.model.ModelHttpResponse;
import com.sigpwned.httpmodel.core.model.ModelHttpResponseHead;

public abstract class DefaultModelHttpClientBase {
  private final ModelHttpConnector connector;
  private final List<ModelHttpRequestFilter> requestFilters;
  private final List<ModelHttpRequestInterceptor> requestInterceptors;
  private final List<ModelHttpResponseFilter> responseFilters;
  private final List<ModelHttpResponseInterceptor> responseInterceptors;

  public DefaultModelHttpClientBase(ModelHttpConnector connector) {
    if (connector == null)
      throw new NullPointerException();
    this.connector = connector;
    this.requestFilters = new ArrayList<>();
    this.requestInterceptors = new ArrayList<>();
    this.responseFilters = new ArrayList<>();
    this.responseInterceptors = new ArrayList<>();
  }

  /**
   * hook
   */
  protected void doRequestFilters(ModelHttpRequestHead requestHead) throws IOException {
    for (ModelHttpRequestFilter requestFilter : getRequestFilters())
      requestFilter.filter(requestHead);
  }

  /**
   * hook
   */
  protected void doRequestInterceptors(ModelHttpRequest request) throws IOException {
    for (ModelHttpRequestInterceptor requestInterceptor : getRequestInterceptors())
      requestInterceptor.intercept(request);
  }

  /**
   * hook
   */
  protected void doResponseFilters(ModelHttpResponseHead responseHead) throws IOException {
    for (ModelHttpResponseFilter responseFilter : getResponseFilters())
      responseFilter.filter(responseHead);
  }

  /**
   * hook
   */
  protected void doResponseInterceptors(ModelHttpResponse response) throws IOException {
    for (ModelHttpResponseInterceptor responseInterceptor : getResponseInterceptors())
      responseInterceptor.intercept(response);
  }

  private static final Comparator<ModelHttpRequestFilter> REQUEST_FILTER_COMPARATOR =
      Comparator.comparingInt(ModelHttpRequestFilter::priority);

  public void addRequestFilter(ModelHttpRequestFilter requestFilter) {
    if (requestFilter == null)
      throw new NullPointerException();
    requestFilters.add(requestFilter);
    requestFilters.sort(REQUEST_FILTER_COMPARATOR);
  }

  public void removeRequestFilter(ModelHttpRequestFilter requestFilter) {
    requestFilters.remove(requestFilter);
  }

  private static final Comparator<ModelHttpRequestInterceptor> REQUEST_INTERCEPTOR_COMPARATOR =
      Comparator.comparingInt(ModelHttpRequestInterceptor::priority);

  public void addRequestInterceptor(ModelHttpRequestInterceptor requestInterceptor) {
    if (requestInterceptor == null)
      throw new NullPointerException();
    requestInterceptors.add(requestInterceptor);
    requestInterceptors.sort(REQUEST_INTERCEPTOR_COMPARATOR);
  }

  public void removeRequestInterceptor(ModelHttpRequestInterceptor requestInterceptor) {
    requestInterceptors.remove(requestInterceptor);
  }

  private static final Comparator<ModelHttpResponseFilter> RESPONSE_FILTER_COMPARATOR =
      Comparator.comparingInt(ModelHttpResponseFilter::priority);

  public void addResponseFilter(ModelHttpResponseFilter responseFilter) {
    if (responseFilter == null)
      throw new NullPointerException();
    responseFilters.add(responseFilter);
    responseFilters.sort(RESPONSE_FILTER_COMPARATOR);
  }

  public void removeResponseFilter(ModelHttpResponseFilter responseFilter) {
    responseFilters.remove(responseFilter);
  }

  private static final Comparator<ModelHttpResponseInterceptor> RESPONSE_INTERCEPTOR_COMPARATOR =
      Comparator.comparingInt(ModelHttpResponseInterceptor::priority);

  public void addResponseInterceptor(ModelHttpResponseInterceptor responseInterceptor) {
    if (responseInterceptor == null)
      throw new NullPointerException();
    responseInterceptors.add(responseInterceptor);
    responseInterceptors.sort(RESPONSE_INTERCEPTOR_COMPARATOR);
  }

  public void removeResponseInterceptor(ModelHttpResponseInterceptor responseInterceptor) {
    responseInterceptors.remove(responseInterceptor);
  }

  protected List<ModelHttpRequestFilter> getRequestFilters() {
    return unmodifiableList(requestFilters);
  }

  protected List<ModelHttpRequestInterceptor> getRequestInterceptors() {
    return unmodifiableList(requestInterceptors);
  }

  protected List<ModelHttpResponseFilter> getResponseFilters() {
    return unmodifiableList(responseFilters);
  }

  protected List<ModelHttpResponseInterceptor> getResponseInterceptors() {
    return unmodifiableList(responseInterceptors);
  }

  protected ModelHttpConnector getConnector() {
    return connector;
  }

  public void close() throws IOException {
    getConnector().close();
  }
}
