# Knot.x Server API
This module defines contracts and factories interfaces for creating [Handlers](https://vertx.io/docs/apidocs/io/vertx/core/Handler.html)
used during the server route processing.

## Contracts
Please refer to the [Data Objects docs](https://github.com/Knotx/knotx-server-http/docs/asciidoc/dataobjects.adoc)
for the detailed information about API contracts.

## Routing Handlers
`Routing Hanlders` are the basic [Handlers](https://vertx.io/docs/apidocs/io/vertx/core/Handler.html) that can be plugged
into the route processing and operate on the [`RoutingContext`](https://vertx.io/docs/apidocs/io/vertx/ext/web/RoutingContext.html).

[`RequestEventHandler`](https://github.com/Knotx/knotx-server-http/blob/master/api/src/main/java/io/knotx/server/api/handler/RequestEventHandler.java)
is basic implementation of such handler.

### Creating Routing Handler
Implement [`RoutingHandlerFactory`](https://github.com/Knotx/knotx-server-http/blob/master/api/src/main/java/io/knotx/server/api/handler/RoutingHandlerFactory.java)
that:
 - is identified by `name` in the operations configuration
 - is registered for the Service Loader in `META-INF/services/io.knotx.server.api.handler.RoutingHandlerFactory`
 - creates [`Handler<RoutingContext>`](https://vertx.io/docs/apidocs/io/vertx/core/Handler.html)
 instance that operates on the [`RoutingContext`](https://vertx.io/docs/apidocs/io/vertx/ext/web/RoutingContext.html).

## Security Handlers
[`AuthHandler`](https://vertx.io/docs/apidocs/io/vertx/ext/web/handler/AuthHandler.html) is responsible
for providing authentication for routes.

### Creating Auth Handler
Implement [`AuthHandlerFactory`](https://github.com/Knotx/knotx-server-http/blob/master/api/src/main/java/io/knotx/server/api/security/AuthHandlerFactory.java)
that:
 - is identified by `name` in server security handlers configuration
 - is registered for the Service Loader in `META-INF/services/io.knotx.server.api.security.AuthHandlerFactory`
 - creates [`AuthHandler`](https://vertx.io/docs/apidocs/io/vertx/ext/web/handler/AuthHandler.html) instance corresponding
 to the type defined in the [Open API security scheme](https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.2.md#securitySchemeObject)
