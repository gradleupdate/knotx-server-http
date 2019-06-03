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

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * Describes Knot.x Hystrix metrics options
 */
@DataObject(generateConverter = true, publicConverter = false)
public class HystrixMetricsOptions {

  /**
   * Default flag whether a metrics should be enabled or not = false
   */
  private static final boolean DEFAULT_ENABLE_METRICS = false;

  /**
   * Default endpoint where metrics will be enabled.
   */
  private static final String DEFAULT_METRICS_ENDPOINT = "/hystrix-metrics";

  private boolean enabled;
  private String endpoint;

  /**
   * Default constructor
   */
  public HystrixMetricsOptions() {
    init();
  }

  /**
   * Create an settings from JSON
   *
   * @param json the JSON
   */
  public HystrixMetricsOptions(JsonObject json) {
    init();
    HystrixMetricsOptionsConverter.fromJson(json, this);
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    HystrixMetricsOptionsConverter.toJson(this, json);
    return json;
  }

  private void init() {
    enabled = DEFAULT_ENABLE_METRICS;
    endpoint = DEFAULT_METRICS_ENDPOINT;
  }

  public boolean isEnabled() {
    return enabled;
  }

  /**
   * Enabled/disables Hystrix metrics exposed by Knot.x Server, by default - disabled.
   *
   * @param enabled true - metrics enabled, false if disabled
   * @return reference to this, so the API can be used fluently
   */
  public HystrixMetricsOptions setEnabled(boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  public String getEndpoint() {
    return endpoint;
  }

  /**
   * Sets Hystrix metrics endpoint under which it will expose the metrics. By default {@code
   * /hystrix-metrics}
   *
   * @param endpoint url under which metrics are provided
   * @return reference to this, so the API can be used fluently
   */
  public HystrixMetricsOptions setEndpoint(String endpoint) {
    this.endpoint = endpoint;
    return this;
  }

  @Override
  public String toString() {
    return "HystrixMetricsOptions{" +
        "enabled=" + enabled +
        ", endpoint='" + endpoint + '\'' +
        '}';
  }
}
