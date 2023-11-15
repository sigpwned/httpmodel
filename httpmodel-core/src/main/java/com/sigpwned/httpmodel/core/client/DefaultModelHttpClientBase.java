package com.sigpwned.httpmodel.core.client;



import static java.util.Collections.unmodifiableList;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

  public void addRequestFilter(ModelHttpRequestFilter requestFilter) {
    if (requestFilter == null)
      throw new NullPointerException();
    requestFilters.add(requestFilter);
  }

  public void removeRequestFilter(ModelHttpRequestFilter requestFilter) {
    requestFilters.remove(requestFilter);
  }

  public void addRequestInterceptor(ModelHttpRequestInterceptor requestInterceptor) {
    if (requestInterceptor == null)
      throw new NullPointerException();
    requestInterceptors.add(requestInterceptor);
  }

  public void removeRequestInterceptor(ModelHttpRequestInterceptor requestInterceptor) {
    requestInterceptors.remove(requestInterceptor);
  }

  public void addResponseFilter(ModelHttpResponseFilter responseFilter) {
    if (responseFilter == null)
      throw new NullPointerException();
    responseFilters.add(responseFilter);
  }

  public void removeResponseFilter(ModelHttpResponseFilter responseFilter) {
    responseFilters.remove(responseFilter);
  }

  public void addResponseInterceptor(ModelHttpResponseInterceptor responseInterceptor) {
    if (responseInterceptor == null)
      throw new NullPointerException();
    responseInterceptors.add(responseInterceptor);
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
