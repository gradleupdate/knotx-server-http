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

import io.knotx.server.api.handler.RoutingHandlerFactory;
import io.knotx.server.configuration.RoutingHandlerOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.api.contract.openapi3.OpenAPI3RouterFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.function.Function;
import java.util.stream.Collectors;

class GlobalHandlersProvider {

  private static final Logger LOGGER = LoggerFactory.getLogger(GlobalHandlersProvider.class);

  private final Vertx vertx;
  private final List<RoutingHandlerOptions> globalHandlers;

  GlobalHandlersProvider(Vertx vertx, List<RoutingHandlerOptions> globalHandlers) {
    this.vertx = vertx;
    this.globalHandlers = globalHandlers;
  }

  void configureGlobalHandlers(OpenAPI3RouterFactory routerFactory) {
    final Map<String, RoutingHandlerFactory> factories = loadAuthHandlerFactories();
    globalHandlers.forEach(handler -> {
      RoutingHandlerFactory factory = factories.get(handler.getName());
      routerFactory.addGlobalHandler(factory.create(vertx, handler.getConfig()));
      LOGGER.info("Global handler {} registered", handler.getName());
    });
  }

  private Map<String, RoutingHandlerFactory> loadAuthHandlerFactories() {
    List<RoutingHandlerFactory> routingFactories = new ArrayList<>();
    ServiceLoader.load(RoutingHandlerFactory.class)
        .iterator()
        .forEachRemaining(routingFactories::add);

    return routingFactories.stream()
        .collect(Collectors.toMap(RoutingHandlerFactory::getName, Function.identity()));
  }
}
