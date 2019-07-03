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

import java.util.Set;

import io.knotx.server.api.context.RequestContext;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.http.HttpServerResponse;
import io.vertx.reactivex.ext.web.RoutingContext;

class ResponseWriterHandler implements Handler<RoutingContext> {

  private static final Logger LOGGER = LoggerFactory.getLogger(ResponseWriterHandler.class);

  private final Set<String> allowedResponseHeaders;

  ResponseWriterHandler(Set<String> allowedResponseHeaders) {
    this.allowedResponseHeaders = allowedResponseHeaders;
  }

  @Override
  public void handle(RoutingContext context) {
    RequestContext requestContext = context.get(RequestContext.KEY);
    traceRequest(requestContext);
    try {
      final ServerResponse serverResponse = new ServerResponse(requestContext);

      if (serverResponse.isNotEmptyBody()) {
        serverResponse.end(context.response(), allowedResponseHeaders);
      } else {
        handleEmpty(context);
      }

    } catch (Exception e) {
      handleFatal(context, e);
    }
  }

  private void traceRequest(RequestContext requestContext) {
    if (LOGGER.isTraceEnabled()) {
      LOGGER.trace("Request history: {}", requestContext.toJson().encodePrettily());
    }
  }

  private void handleWithStatusCode(RoutingContext context, int statusCode) {
    HttpServerResponse httpResponse = context.response();
    httpResponse.setStatusCode(statusCode);
    httpResponse.end();
  }

  private void handleEmpty(RoutingContext context) {
    handleWithStatusCode(context, HttpResponseStatus.NO_CONTENT.code());
  }

  private void handleFatal(RoutingContext context, Exception e) {
    LOGGER.error("Fatal error", e);
    handleWithStatusCode(context, HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
  }

}
