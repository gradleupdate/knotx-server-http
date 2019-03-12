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
import io.knotx.server.configuration.RoutingOperationOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.api.contract.openapi3.OpenAPI3RouterFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

class RoutesProvider {

  private static final Logger LOGGER = LoggerFactory.getLogger(RoutesProvider.class);
  private final Vertx vertx;
  private final List<RoutingOperationOptions> routingOperations;

  RoutesProvider(Vertx vertx,
      List<RoutingOperationOptions> routingOperations) {
    this.vertx = vertx;
    this.routingOperations = routingOperations;
    validateRoutingOperations();
  }

  void configureRouting(OpenAPI3RouterFactory routerFactory) {
    List<RoutingHandlerFactory> handlerFactories = loadRoutingHandlerFactories();
    routingOperations.forEach(operation -> {
      registerRoutingHandlersPerOperation(routerFactory, handlerFactories, operation);
      registerFailureHandlersPerOperation(routerFactory, handlerFactories, operation);
      LOGGER.info("Initialized all handlers for operation [{}]", operation.getOperationId());
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("Operation initialization details [{}]", operation.toJson().encodePrettily());
      }
    });
  }

  private void validateRoutingOperations() {
    if (routingOperations == null || routingOperations.isEmpty()) {
      LOGGER.warn(
          "The server configuration does not contain any operation defined. Please check your "
              + "configuration [config.server.options.config.routingOperations]");
    }
  }

  private void registerRoutingHandlersPerOperation(OpenAPI3RouterFactory routerFactory,
      List<RoutingHandlerFactory> handlerFactories,
      RoutingOperationOptions operation) {
    operation.getHandlers().forEach(routingHandlerOptions ->
        handlerFactories.stream()
            .filter(
                handlerFactory -> handlerFactory.getName()
                    .equals(routingHandlerOptions.getName()))
            .findAny()
            .map(handlerFactory ->
                routerFactory
                    .addHandlerByOperationId(operation.getOperationId(),
                        handlerFactory.create(vertx, routingHandlerOptions.getConfig()))
            )
            .orElseThrow(() -> {
              LOGGER.error(
                  "Handler factory [{}] not found in registered factories [{}], all options [{}]",
                  routingHandlerOptions.getName(), handlerFactories, operation);
              return new IllegalStateException(
                  "Can not find handler factory for [" + routingHandlerOptions.getName() + "]");
            })
    );
  }

  private void registerFailureHandlersPerOperation(OpenAPI3RouterFactory openAPI3RouterFactory,
      List<RoutingHandlerFactory> handlerFactories,
      RoutingOperationOptions routingOperationOptions) {
    routingOperationOptions.getFailureHandlers().forEach(routingHandlerOptions ->
        handlerFactories.stream()
            .filter(
                handlerFactory -> handlerFactory.getName()
                    .equals(routingHandlerOptions.getName()))
            .findAny()
            .map(handlerFactory ->
                openAPI3RouterFactory
                    .addFailureHandlerByOperationId(routingOperationOptions.getOperationId(),
                        handlerFactory.create(vertx, routingHandlerOptions.getConfig()))
            )
            .orElseThrow(IllegalStateException::new)
    );
  }

  private List<RoutingHandlerFactory> loadRoutingHandlerFactories() {
    List<RoutingHandlerFactory> routingFactories = new ArrayList<>();
    ServiceLoader.load(RoutingHandlerFactory.class)
        .iterator()
        .forEachRemaining(routingFactories::add);

    LOGGER.info("Routing handler factory names [{}] registered.",
        routingFactories.stream().map(RoutingHandlerFactory::getName).collect(Collectors
            .joining(",")));

    return routingFactories;
  }
}
