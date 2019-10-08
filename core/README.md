# Knot.x Server implementation
This module contains implementation of a Knot.x HTTP Server which is a [verticle](http://vertx.io/docs/apidocs/io/vertx/core/Verticle.html)
that listens for HTTP requests and is responsible for handling the whole HTTP traffic that goes to 
the Knot.x instance.

Read more about the Server itself in the (How does it work)(https://github.com/Knotx/knotx-server-http#how-does-it-work)
section of the main Readme file.

## Handlers
This module contains also some basic handlers implementations with factories:
- `BodyRoutingHandler` - enables to set [request body options](https://github.com/Knotx/knotx-server-http/blob/master/core/docs/asciidoc/dataobjects.adoc#bodyhandleroptions)
- `RequestContextRoutingHandler` - creates [`RequestContext`](https://github.com/Knotx/knotx-server-http/blob/master/api/docs/asciidoc/dataobjects.adoc#requestcontext) 
instance and saves it into the [`RoutingContext`](https://vertx.io/docs/apidocs/io/vertx/ext/web/RoutingContext.html) 
under `requestContext` key.
- `CustomHeaderRoutingHanlder` - adds custom header to each response Knot.x produces, configured via 
[`Custom Header Options`](https://github.com/Knotx/knotx-server-http/blob/master/core/docs/asciidoc/dataobjects.adoc#customhttpheaderoptions)
- `ResponseWriterHandler` - uses [Request Context](https://github.com/Knotx/knotx-server-http/blob/master/api/docs/asciidoc/dataobjects.adoc#requestcontext)
client response to build final response and sends it to the end user.
- `CSRFHandler` - adds a CSRF token to requests which mutate state, configured via [`CSRFOptions`](https://github.com/Knotx/knotx-server-http/blob/master/core/docs/asciidoc/dataobjects.adoc#csrfoptions).