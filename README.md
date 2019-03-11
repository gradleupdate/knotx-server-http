# Knot.x HTTP Server
Server is essentially a "heart" of Knot.x. It creates HTTP Server, listening for requests
and is responsible for handling the whole HTTP traffic that goes to the Knot.x instance. 
It's scalable (vertically and horizontally), plugable (easy to extend), resilient to errors 
and highly efficient reactive server based on [Vert.x](https://vertx.io/).

As every HTTP server, Knot.x HTTP Server enables to define routes it supports. Knot.x values
Design First Approach, so defining routes and security is done with [Open API 3](https://github.com/OAI/OpenAPI-Specification)
standard.

What Knot.x Server enables is plugging in custom behaviour for each supported route.
Each defined [`path`](https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.2.md#pathItemObject)\
is processed by the chain of [Handlers](https://vertx.io/docs/apidocs/io/vertx/core/Handler.html)
that reacts on the request and are able to shape the response.

[ToDo diagram here]

## Modules
Aside of the [Server API](https://github.com/Knotx/knotx-server-http/tree/master/api) and 
[Server core](https://github.com/Knotx/knotx-server-http/tree/master/core) this repository contains
core handlers implementations:
- [HTML Splitter](https://github.com/Knotx/knotx-server-http/tree/master/splitter-html) responsible for
extracting [`Fragments`](https://github.com/Knotx/knotx-fragment-api) from the template
- [Assembler](https://github.com/Knotx/knotx-server-http/tree/master/assembler) that combines all
processed fragments into the response body.

## How does it work

## How to configure

## Handlers

### Creating custom handler