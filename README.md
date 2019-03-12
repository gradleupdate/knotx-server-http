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
- [Assembler](https://github.com/Knotx/knotx-server-http/tree/master/assembler) that combines all
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

### Server options

#### Dropping the requests

### Routing Specification

### Routing Operations

#### Handling failures

####

####

## Handlers

### Creating custom handler