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
package io.knotx.server;

import io.knotx.server.configuration.DropRequestOptions;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.SingleSource;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.RxHelper;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.core.http.HttpServerRequest;
import io.vertx.reactivex.ext.web.Router;

class HttpServerProvider {

  private static final Logger LOGGER = LoggerFactory.getLogger(HttpServerProvider.class);
  private final Vertx vertx;
  private final HttpServerOptions serverOptions;
  private final DropRequestOptions dropRequestOptions;

  HttpServerProvider(Vertx vertx, HttpServerOptions serverOptions,
      DropRequestOptions dropRequestOptions) {
    this.vertx = vertx;
    this.serverOptions = serverOptions;
    this.dropRequestOptions = dropRequestOptions;
  }

  SingleSource<? extends HttpServer> configureHttpServer(Router router) {
    HttpServer httpServer = vertx.createHttpServer(serverOptions);

    if (dropRequestOptions.isEnabled()) {
      httpServer.requestStream().toFlowable()
          .map(HttpServerRequest::pause)
          .onBackpressureBuffer(dropRequestOptions.getBackpressureBufferCapacity(),
              () -> LOGGER.warn("Backpressure buffer is overflown. Dropping request"),
              dropRequestOptions.getBackpressureStrategy())
          .onBackpressureDrop(
              req -> req.response().setStatusCode(dropRequestOptions.getDropRequestResponseCode())
                  .end())
          .observeOn(RxHelper.scheduler(vertx.getDelegate()))
          .subscribe(req -> {
            req.resume();
            routeSafe(req, router);
          }, error -> LOGGER.error("Exception while processing!", error));
    } else {
      httpServer
          .requestHandler(req -> routeSafe(req, router));
    }

    return httpServer.rxListen();
  }

  private void routeSafe(HttpServerRequest req, Router router) {
    try {
      router.handle(req);
    } catch (IllegalArgumentException ex) {
      LOGGER.warn("Problem decoding Query String ", ex);

      req.response()
          .setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
          .setStatusMessage(HttpResponseStatus.BAD_REQUEST.reasonPhrase())
          .end("Invalid characters in Query Parameter");
    }
  }

}
