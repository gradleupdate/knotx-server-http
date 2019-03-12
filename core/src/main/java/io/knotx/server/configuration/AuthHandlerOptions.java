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

import io.knotx.server.api.security.AuthHandlerFactory;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import org.apache.commons.lang3.StringUtils;

/**
 * Handler definition that contains {@link AuthHandlerFactory} name and JSON configuration. During
 * {@link io.knotx.server.KnotxServerVerticle} deployment all implementations of {@link
 * AuthHandlerFactory} are loaded from the classpath and based on {@link
 * AuthHandlerFactory#getName()} are initiated.
 */
@DataObject(generateConverter = true, publicConverter = false)
public class AuthHandlerOptions {

  private String schema;
  private String factory;
  private JsonObject config;

  /**
   * Create settings from JSON
   *
   * @param json the JSON
   */
  public AuthHandlerOptions(JsonObject json) {
    this.config = new JsonObject();
    AuthHandlerOptionsConverter.fromJson(json, this);
    if (StringUtils.isAnyBlank(schema, factory)) {
      throw new IllegalArgumentException("Auth Handler schema and factory names must be set!");
    }
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    AuthHandlerOptionsConverter.toJson(this, json);
    return json;
  }

  /**
   * @return security schema name
   */
  public String getSchema() {
    return schema;
  }

  /**
   * Sets security schema name
   *
   * @param schema security schema name
   * @return reference to this, so the API can be used fluently
   */
  public AuthHandlerOptions setSchema(String schema) {
    this.schema = schema;
    return this;
  }

  /**
   * @return {@link AuthHandlerFactory} name
   */
  public String getFactory() {
    return factory;
  }

  /**
   * Sets {@link AuthHandlerFactory} name
   *
   * @param factory handler factory name
   * @return reference to this, so the API can be used fluently
   */
  public AuthHandlerOptions setFactory(String factory) {
    this.factory = factory;
    return this;
  }

  /**
   * @return JSON configuration used during {@link AuthHandlerFactory#create(Vertx, JsonObject)}
   * initialization
   */
  public JsonObject getConfig() {
    return config == null ? new JsonObject() : config;
  }

  /**
   * Sets {@link io.vertx.core.Handler} configuration.
   *
   * @param config handler JSON configuration
   * @return reference to this, so the API can be used fluently
   */
  public AuthHandlerOptions setConfig(JsonObject config) {
    this.config = config;
    return this;
  }

  @Override
  public String toString() {
    return "AuthHandlerOptions{" +
        "schema='" + schema + '\'' +
        ", factory='" + factory + '\'' +
        ", config=" + config +
        '}';
  }
}
