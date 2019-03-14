# Knot.x HTTP Server
Server is essentially a "heart" of Knot.x. It's scalable (vertically and horizontally), 
plugable (easy to extend), resilient to errors 
and highly efficient reactive server based on [Vert.x](https://vertx.io/).

As every HTTP server, Knot.x HTTP Server enables defining routes it supports. Defining routes 
and security is done with [Open API 3](https://github.com/OAI/OpenAPI-Specification) standard.

What Knot.x Server enables is plugging in custom behaviour for each supported route.
Each defined [`path`](https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.2.md#pathItemObject)\
is processed by the chain of [Handlers](https://vertx.io/docs/apidocs/io/vertx/core/Handler.html)
that reacts on the request and are able to shape the response.

![server flow](misc/server-flow.png)

## Modules
Aside of the [Server API](https://github.com/Knotx/knotx-server-http/tree/master/api) and 
[Server core](https://github.com/Knotx/knotx-server-http/tree/master/core) this repository contains
core handlers implementations:
- [HTML Splitter](https://github.com/Knotx/knotx-server-http/tree/master/splitter-html) responsible for
extracting [`Fragments`](https://github.com/Knotx/knotx-fragment-api) from the template
- [Assembler](https://github.com/Knotx/knotx-server-http/tree/master/assembler) combines all
processed [`Fragments`](https://github.com/Knotx/knotx-fragment-api) into the response body.

## How does it work
Knot.x HTTP Server is a [verticle](http://vertx.io/docs/apidocs/io/vertx/core/Verticle.html)
that listens for HTTP requests. It is responsible for handling the whole HTTP traffic that goes to 
the Knot.x instance.

Once the HTTP request from comes to the Server following actions are happening:
- Server checks if there isn't too many concurrent requests. If the system is overloaded, 
[incoming request is dropped](#dropping-the-requests).
- Server checks if there is [routing path](#routing-specification) defined for incoming request path
and method. If no path supports this request, `404 Not Found` is returned in response.
- Server checks if there is [routing operation](#routing-operations) defined for incoming request.
If there are no handlers defined in the operations for this route, `501 Not Implemented` is returned in response.
- Handlers processing starts. In case of operation handler failure, [failure handlers](#handling-failures)
are triggered.
- Handling is continued until any Handler end the response. If none does, `500 Internal Server Error`
is returned.

## How to configure
To run Knot.x HTTP Server, simply add an entry in the [`modules` configuration](https://github.com/Knotx/knotx-launcher#modules-configuration):

```hocon
modules = {
  server = "io.knotx.server.KnotxServerVerticle"
}
```

Details of the Knot.x HTTP Server can be configured with [`KnotxServerOptions`](/core/docs/asciidoc/dataobjects.adoc#knotxserveroptions):
in the configuration file:
```hocon
config.server.options.config {
  serverOptions {
    port = 8080
  }
}
```

### Http Server Options
HTTP Options can be configured with [Vert.x `HttpServerOptions`](http://vertx.io/docs/vertx-core/dataobjects.html#HttpServerOptions).
E.g. following configuration defines the port Server is listening at:
```hocon
config.server.options.config {
  serverOptions {
    port = 8080
  }
}
```
#### Server Port Configuration 
A HTTP server port can be also specified through system property `knotx.port` that takes 
precedence over the value in the configuration file.
```
java -Dknotx.port=9999 ...
```

### Dropping the requests
Knot.x Server implements a backpressure mechanism. It allows to drop requests after exceeding a 
certain amount of requests at a time.
If the the Server can't process incoming requests fast enough (requests buffer limit is reached), 
Server drops requests and respond with configurable response code (by default `429, "Too Many Requests"`).

Dropping requests can be configured with [`DropRequestOptions`](core/docs/asciidoc/dataobjects.adoc#droprequestoptions)

After the buffer slots will be released the new requests will start to be accepted and finally processed.

> That solution prevent `OutOfMemoryError` errors when there are too many requests (e.g. during the peak hours). 
Additionally response times should be more stable when system is under high stress.

### Routing Specification

### Routing Operations

#### Handling failures

### Routing Security

## Handlers

### Creating custom handler