# HTTPMODEL [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.sigpwned/httpmodel-core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.sigpwned/httpmodel-core)

## Motivation

Many HTTP operations, like signing requests, are library agnostic and require requests to be resident in memory. Implementing these operations against multiple library implementations is wasteful and can be difficult for libraries designed to work with large request and response bodies. The httpmodel library is designed to allow library writers to build library-agnostic models once, and then simply allow users to convert their models back and forth between the httpmodel object model.

## Goals

* Define a library-agnostic model of HTTP primitive objects, like requests, headers, etc.
* Provide conversion routines between the model and the most popular HTTP client and server libraries

## Non-Goals

* Build a new HTTP client implementation
* Support all HTTP libraries

## Examples

### Servlets

Users can convert back and forth between servlet objects like this:

    public ModelHttpRequest convertToHttpModel(HttpServletRequest request) {
        return ModelHttpServlets.fromRequest(request);
    }

    public HttpServletResponse populateServletResponse(HttpModelResponse response, HttpServletResponse s) {
        return ModelHttpServlets.toResponse(s, response);
    }

### Java 11 Client

Users can convert back and forth between Java 11 `HttpClient` objects like this:

    public HttpRequest<byte[]> convertToHttpRequest(ModelHttpRequest request) {
        return ModelHttpClients.toRequest(request);
    }

    public ModelHttpResponse convertFromHttpResponse(HttpResponse<byte[]> response) {
        return ModelHttpClients.fromResponse(response);
    }

### HttpURLConnection

Users can convert back and forth between `HttpURLConnection` objects like this:

    public HttpURLConnection convertToHttpURLConnection(ModelHttpRequest request) {
        return ModelHttpURLConnections.toRequest(request);
    }

    public ModelHttpResponse convertFromHttpURLConnection(HttpURLConnection connection) {
        return ModelHttpURLConnections.fromResponse(connection);
    }