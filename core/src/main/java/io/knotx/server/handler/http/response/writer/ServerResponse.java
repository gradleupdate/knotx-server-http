/*
 * Copyright (C) 2019 Knot.x Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.knotx.server.handler.http.response.writer;

import io.knotx.server.api.context.ClientResponse;
import io.knotx.server.api.context.RequestContext;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpStatusClass;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpHeaders;
import io.vertx.reactivex.core.MultiMap;
import io.vertx.reactivex.core.http.HttpServerResponse;
import java.util.Optional;
import java.util.Set;

class ServerResponse {

  private ClientResponse clientResponse;
  private boolean ok;

  ServerResponse(RequestContext requestContext) {
    this.clientResponse = requestContext.getClientResponse();
    this.ok = isOk(requestContext);
  }

  void end(HttpServerResponse httpResponse, Set<String> allowedResponseHeaders) {
    httpResponse.setStatusCode(getStatusCode());
    if (ok) {
      httpResponse.headers().addAll(getHeaders(allowedResponseHeaders));
      clearContentLengthHeader(httpResponse);
      if (getBody().isPresent()) {
        httpResponse.end(getBody().get());
      } else {
        httpResponse.end();
      }
    } else {
      httpResponse.end();
    }
  }

  /*
   * Removes content-length from the final response after writing headers from the client response.
   * The length that was in client response might come from the repository and is almost always
   * different than the actual body length after full processing. The content-length header should
   * be automatically calculated by the httpResponse.
   */
  private void clearContentLengthHeader(HttpServerResponse httpResponse) {
    httpResponse.headers().remove(HttpHeaders.CONTENT_LENGTH.toString());
  }

  private int getStatusCode() {
    int statusCode;
    final HttpStatusClass httpStatusClass = HttpStatusClass.valueOf(clientResponse.getStatusCode());
    if (httpStatusClass != HttpStatusClass.UNKNOWN) {
      statusCode = clientResponse.getStatusCode();
    } else {
      statusCode = HttpResponseStatus.INTERNAL_SERVER_ERROR.code();
    }
    return statusCode;
  }

  private MultiMap getHeaders(Set<String> allowedResponseHeaders) {
    MultiMap headers = MultiMap.caseInsensitiveMultiMap();
    if (clientResponse.getHeaders() != null) {
      clientResponse.getHeaders().names().stream()
          .filter(header -> headerFilter(allowedResponseHeaders, header))
          .forEach(
              name ->
                  clientResponse.getHeaders()
                      .getAll(name)
                      .forEach(value -> headers.add(name, value))
          );
    }
    return headers;
  }

  private Optional<io.vertx.reactivex.core.buffer.Buffer> getBody() {
    final Buffer crBody = clientResponse.getBody();
    io.vertx.reactivex.core.buffer.Buffer result = null;
    if (crBody != null) {
      result = io.vertx.reactivex.core.buffer.Buffer.newInstance(crBody);
    }
    return Optional.ofNullable(result);
  }

  private static boolean isOk(RequestContext requestContext) {
    return !requestContext.status().isFailed();
  }

  private Boolean headerFilter(Set<String> allowedResponseHeaders, String name) {
    return allowedResponseHeaders.contains(name.toLowerCase());
  }

}
