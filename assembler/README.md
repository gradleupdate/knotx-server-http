# Knot.x Assembler Handler
This module contains [Handler](https://vertx.io/docs/apidocs/io/vertx/core/Handler.html)
that joins all [`Fragments`](https://github.com/Knotx/knotx-fragment-api#Fragmrnt) and
saves the result into the [`ClientRequest`](api/docs/asciidoc/dataobjects.adoc#clientresponse) body.

## How does it work?
Fragment Assembler reads Fragments from the [`RequestEvent`](https://github.com/Knotx/knotx-server-http/blob/master/api/src/main/java/io/knotx/server/api/context/RequestEvent.java)
and joins them all into one string.

### How Fragments are being joined?
Lets explain process of fragments join using example. Fragment Assembler reads [`ClientRequest`](api/docs/asciidoc/dataobjects.adoc#clientresponse)
that contains three Fragments:
```html
<html>
<head>
  <title>Test</title>
</head>
<body>
<h1>test</h1>
```
```html
  <h2>this is webservice no. 1</h2>
  <div>message - a</div>
```
```html
</body>
</html>
```
Fragment Assembler joins all those Fragments into one string:
```html
<html>
<head>
  <title>Test</title>
</head>
<body>
<h1>test</h1>
  <h2>this is webservice no. 1</h2>
  <div>message - a</div>
</body>
</html>
```