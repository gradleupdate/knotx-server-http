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
You will find docs in the `README.md` files inside each of following modules:
- [Server API](https://github.com/Knotx/knotx-server-http/tree/master/api) that defines contracts. 
- [Server core](https://github.com/Knotx/knotx-server-http/tree/master/core) that contains some smaller 
handlers and HTTP Server implementation.
- [HTML Splitter](https://github.com/Knotx/knotx-server-http/tree/master/splitter-html) handler 
responsible for extracting [`Fragments`](https://github.com/Knotx/knotx-fragment-api) from the template
- [Assembler](https://github.com/Knotx/knotx-server-http/tree/master/assembler) handler combines all
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
Knot.x Routing is defined with [Open API 3](https://github.com/OAI/OpenAPI-Specification) standard. 
`routingSpecificationLocation` is a path to the Open API YAML specification.
It can be an absolute path, a local path or remote url (with HTTP protocol).

```hocon
config.server.options.config {
  routingSpecificationLocation = /openapi.yaml
}
```

### Routing Operations
A [`Routing Operation`](core/docs/asciidoc/dataobjects.adoc#routingoperationoptions) 
define handlers and failure handlers taking part in HTTP request processing of a particular 
[operation](https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.2.md#operationObject)
defined in the [Routing Specification](#routing-specification) (linking is done
by `operationId`):

> Open Api YAML config:
```yaml
...
paths:
  /example*:
    get:
      operationId: my-example-operation
...
```
> Routing Operations entry in Knot.x conf
```hocon
config.server.options.configroutingOperations = ${routingOperations} [
  {
    operationId = my-example-operation
    handlers = [
      # list of handlers that handles the request in chain
    ]
    failureHandlers = [
      # list of failure handlers
    ]
  }
]
```

### Routing Security
Security for each operation defined in [operation security requirement](https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.2.md#security-requirement-object)
is implemented in a form of [`AuthHandlers`](https://vertx.io/docs/apidocs/io/vertx/ext/web/handler/AuthHandler.html).
In order to introduce security to operation you need to implement [`AuthHandlerFactory`](https://github.com/Knotx/knotx-server-http/blob/master/api/src/main/java/io/knotx/server/api/security/AuthHandlerFactory.java)
that provides `AuthHandler` of a proper type.

#### Example security setup
> Open Api YAML config:
```yaml
...
paths:
  /example*:
    get:
      operationId: my-example-operation
      security:
          - myExampleBasicAuth: []
...
components:
  securitySchemes:
    myExampleBasicAuth:
      type: http
      scheme: basic
```

> Routing Operations entry in Knot.x conf
```hocon
config.server.options.config.securityHandlers = [
  {
    schema = myExampleBasicAuth
    factory = myBasicAuthHandlerFactory
    config = {
      # any config passed to the AuthHandler
    }
  }
]
```

Needs implementation of [`AuthHandlerFactory`](https://github.com/Knotx/knotx-server-http/blob/master/api/src/main/java/io/knotx/server/api/security/AuthHandlerFactory.java)
that:
 - `name` is `myBasicAuthHandlerFactory`
 - is registered for the Service Loader in `META-INF/services/io.knotx.server.api.security.AuthHandlerFactory`
 - creates [`AuthHandler`](https://vertx.io/docs/apidocs/io/vertx/ext/web/handler/AuthHandler.html) instance corresponding
 to the type defined in the [Open API security scheme](https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.2.md#securitySchemeObject)
