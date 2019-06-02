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
package io.knotx.server.configuration;

import static io.knotx.server.KnotxServerVerticle.KNOTX_PORT_PROP_NAME;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.api.contract.openapi3.OpenAPI3RouterFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Describes a Knot.x HTTP Server configuration
 */
@DataObject(generateConverter = true, publicConverter = false)
public class KnotxServerOptions {

  /**
   * Default flag if to show the exceptions on error pages = false
   */
  private static final boolean DEFAULT_DISPLAY_EXCEPTIONS = false;
  private static final boolean DEFAULT_HYSTRIX_METRICS = false;

  private boolean displayExceptionDetails;
  private boolean enableHystrixMetrics;
  private HttpServerOptions serverOptions;
  private String routingSpecificationLocation;
  private List<RoutingHandlerOptions> globalHandlers;
  private List<AuthHandlerOptions> securityHandlers;
  private List<RoutingOperationOptions> routingOperations;
  private DropRequestOptions dropRequestOptions;

  /**
   * Default constructor
   */
  public KnotxServerOptions() {
    init();
  }

  /**
   * Copy constructor
   *
   * @param other the instance to copy
   */
  public KnotxServerOptions(KnotxServerOptions other) {
    this.displayExceptionDetails = other.displayExceptionDetails;
    this.enableHystrixMetrics = other.enableHystrixMetrics;
    this.serverOptions = new HttpServerOptions(other.serverOptions);
    this.routingSpecificationLocation = other.routingSpecificationLocation;
    this.globalHandlers = new ArrayList<>(other.globalHandlers);
    this.securityHandlers = new ArrayList<>(other.securityHandlers);
    this.routingOperations = new ArrayList<>(other.routingOperations);
    this.dropRequestOptions = other.dropRequestOptions;
  }

  /**
   * Create an settings from JSON
   *
   * @param json the JSON
   */
  public KnotxServerOptions(JsonObject json) {
    init();
    KnotxServerOptionsConverter.fromJson(json, this);

    //port was specified in config, try to overwrite with system props if defined
    serverOptions.setPort(Integer.getInteger(KNOTX_PORT_PROP_NAME, serverOptions.getPort()));
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    KnotxServerOptionsConverter.toJson(this, json);
    return json;
  }

  private void init() {
    displayExceptionDetails = DEFAULT_DISPLAY_EXCEPTIONS;
    enableHystrixMetrics = DEFAULT_HYSTRIX_METRICS;
    globalHandlers = new ArrayList<>();
    securityHandlers = new ArrayList<>();
    serverOptions = new HttpServerOptions();
    dropRequestOptions = new DropRequestOptions();
  }

  /**
   * @return whether to display or not exceptions on error pages
   */
  public boolean isDisplayExceptionDetails() {
    return displayExceptionDetails;
  }

  /**
   * Set whether to display or not the exception on error pages
   *
   * @param displayExceptionDetails displays exceptions on error pages if true
   * @return reference to this, so the API can be used fluently
   */
  public KnotxServerOptions setDisplayExceptionDetails(boolean displayExceptionDetails) {
    this.displayExceptionDetails = displayExceptionDetails;
    return this;
  }

  /**
   * @return whether to setup or not hystrix metrics
   */
  public boolean isEnableHystrixMetrics() {
    return enableHystrixMetrics;
  }

  /**
   * Set whether to setup or not hystrix metrics
   *
   * @param enableHystrixMetrics expose hystrics metrics if true
   * @return reference to this, so the API can be used fluently
   */
  public KnotxServerOptions setEnableHystrixMetrics(boolean enableHystrixMetrics) {
    this.enableHystrixMetrics = enableHystrixMetrics;
    return this;
  }

  /**
   * @return {@code io.vertx.core.http.HttpServerOptions}
   */
  public HttpServerOptions getServerOptions() {
    return serverOptions;
  }

  /**
   * Set the HTTP Server options
   *
   * @param serverOptions {@code io.vertx.core.http.HttpServerOptions} object
   * @return reference to this, so the API can be used fluently
   */
  public KnotxServerOptions setServerOptions(HttpServerOptions serverOptions) {
    this.serverOptions = serverOptions;
    return this;
  }

  /**
   * @return location of your spec. It can be an absolute path, a local path or remote url (with
   * HTTP protocol)
   */
  public String getRoutingSpecificationLocation() {
    return routingSpecificationLocation;
  }

  /**
   * Location of your spec. It can be an absolute path, a local path or remote url (with HTTP
   * protocol).
   *
   * @param routingSpecificationLocation routing specification location
   * @return reference to this, so the API can be used fluently
   * @see OpenAPI3RouterFactory#rxCreate(Vertx, String)
   */
  public KnotxServerOptions setRoutingSpecificationLocation(
      String routingSpecificationLocation) {
    this.routingSpecificationLocation = routingSpecificationLocation;
    return this;
  }

  /**
   * @return list of global routing operations options
   */
  public List<RoutingHandlerOptions> getGlobalHandlers() {
    return globalHandlers;
  }

  /**
   * List of {@code RoutingHandlerOptions} containing handlers configurations which are initiated
   * (loaded from classpath via {@code java.util.ServiceLoader}) during server setup and applied to
   * each route.
   *
   * @param globalHandlers global routing operations
   * @return reference to this, so the API can be used fluently
   */
  public KnotxServerOptions setGlobalHandlers(List<RoutingHandlerOptions> globalHandlers) {
    this.globalHandlers = globalHandlers;
    return this;
  }

  /**
   * List of {@code AuthHandlerOptions} containing auth handlers configurations which are initiated
   * (loaded from classpath via {@code java.util.ServiceLoader}) during server setup and joined with
   * Open API security schemas based on {@code AuthHandlerOptions} schema name.
   *
   * @return list of auth handlers options
   */
  public List<AuthHandlerOptions> getSecurityHandlers() {
    return securityHandlers;
  }

  /**
   * Set list of {@code AuthHandlerOptions}.
   *
   * @param securityHandlers auth handlers options
   * @return reference to this, so the API can be used fluently
   */
  public KnotxServerOptions setSecurityHandlers(List<AuthHandlerOptions> securityHandlers) {
    this.securityHandlers = securityHandlers;
    return this;
  }

  /**
   * List of {@code RoutingOperationOptions} containing handlers configurations which are initiated
   * (loaded from classpath via {@code java.util.ServiceLoader}) during server setup and joined with
   * Open API operations based on operationId.
   *
   * @return list of routing operations options
   */
  public List<RoutingOperationOptions> getRoutingOperations() {
    return routingOperations;
  }

  /**
   * Set list of {@code RoutingOperationOptions}.
   *
   * @param routingOperations routing operations options
   * @return reference to this, so the API can be used fluently
   */
  public KnotxServerOptions setRoutingOperations(
      List<RoutingOperationOptions> routingOperations) {
    this.routingOperations = routingOperations;
    return this;
  }

  /**
   * @return a {@code DropRequestOptions} configuration
   */
  public DropRequestOptions getDropRequestOptions() {
    return dropRequestOptions;
  }

  /**
   * Set the drop request options
   *
   * @param dropRequestOptions a {@code DropRequestOptions} configuration
   * @return reference to this, so the API can be used fluently
   */
  public KnotxServerOptions setDropRequestOptions(DropRequestOptions dropRequestOptions) {
    this.dropRequestOptions = dropRequestOptions;
    return this;
  }

}
