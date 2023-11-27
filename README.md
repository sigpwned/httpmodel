# HTTPMODEL [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.sigpwned/httpmodel-core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.sigpwned/httpmodel-core) [![tests](https://github.com/sigpwned/httpmodel/actions/workflows/tests.yml/badge.svg)](https://github.com/sigpwned/httpmodel/actions/workflows/tests.yml)

## Motivation

Many HTTP operations, like signing requests, are library agnostic. Implementing these operations against multiple libraries is wasteful. The httpmodel library is designed to allow library writers to build library-agnostic models once, and then simply allow users to convert their models back and forth between the httpmodel object model.

The httpmodel library also offers a client SPI with interchangeable transport backends for those who prefer to do work using the model itself.

## Goals

* Define a library-agnostic model of HTTP primitive objects, like requests, headers, etc.
* Provide conversion routines between the model and the most popular HTTP client and server libraries
* Expose a simple HTTP client to use the model directly

## Non-Goals

* Build a new HTTP implementation
* Support all HTTP libraries

## Conversion Examples

### Servlets

Users can convert back and forth between servlet objects like this:

    public ModelHttpRequest convertToHttpModel(HttpServletRequest request) {
        return ModelHttpServlets.fromRequest(request);
    }

    public HttpServletResponse populateServletResponse(HttpModelResponse response, HttpServletResponse s) {
        return ModelHttpServlets.toResponse(s, response);
    }

This requires the `httpmodel-servlet` module.

### Java 11 Client

Users can convert back and forth between Java 11 `HttpClient` objects like this:

    public HttpRequest<byte[]> convertToHttpRequest(ModelHttpRequest request) {
        return ModelHttpClients.toRequest(request);
    }

    public ModelHttpResponse convertFromHttpResponse(HttpResponse<byte[]> response) {
        return ModelHttpClients.fromResponse(response);
    }

This requires the `httpmodel-core` module.

### HttpURLConnection

Users can convert back and forth between `HttpURLConnection` objects like this:

    public HttpURLConnection convertToHttpURLConnection(ModelHttpRequest request) {
        return ModelHttpURLConnections.toRequest(request);
    }

    public ModelHttpResponse convertFromHttpURLConnection(HttpURLConnection connection) {
        return ModelHttpURLConnections.fromResponse(connection);
    }

This requires the `httpmodel-core` module.

## HTTP Client Examples

To grab a copy of the Yahoo! frontpage, use this code:

    try (ModelHttpClient client=new DefaultModelHttpClient(new UrlConnectionModelHttpConnector())) {
        try (ModelHttpResponse response=client.send(ModelHttpRequest.builder()
                .method(ModelHttpMethods.GET)
                .url(URI.create("https://www.yahoo.com/"))
                .build())) {
            System.out.println(response.getStatusCode());
            System.out.println(response.toString(StandardCharsets.UTF_8));
        }
    }

This requires the `httpmodel-client` module.
