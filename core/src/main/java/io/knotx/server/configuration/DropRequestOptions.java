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

import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.BackpressureOverflowStrategy;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * Describes Knot.x HTTP Server dropping request options
 */
@DataObject(generateConverter = true, publicConverter = false)
public class DropRequestOptions {

  /**
   * Default flag whether a request dropping on heavy load (backpressure) is enabled or not = false
   */
  private static final boolean DEFAULT_DROP_REQUESTS = false;

  /**
   * Default response status code send for dropped requests = 429 (Too Many Requests)
   */
  private static final int DEFAULT_DROP_REQUESTS_RESPONSE_CODE = HttpResponseStatus.TOO_MANY_REQUESTS
      .code();

  /**
   * Default backpressure buffer size = 1000
   */
  private static final long DEFAULT_BACKPRESSURE_BUFFER_SIZE = 1000L;

  /**
   * Default backpressure strategy = DROP_LATEST
   */
  private static final BackpressureOverflowStrategy DEFAULT_BACKPRESSURE_STRATEGY = BackpressureOverflowStrategy.DROP_LATEST;

  private boolean enabled;
  private int dropRequestResponseCode;
  private long backpressureBufferCapacity;
  private BackpressureOverflowStrategy backpressureStrategy;

  /**
   * Default constructor
   */
  public DropRequestOptions() {
    init();
  }


  /**
   * Copy constructor
   *
   * @param other the instance to copy
   */
  public DropRequestOptions(DropRequestOptions other) {
    this.enabled = other.enabled;
    this.dropRequestResponseCode = other.dropRequestResponseCode;
    this.backpressureBufferCapacity = other.backpressureBufferCapacity;
    this.backpressureStrategy = other.backpressureStrategy;
  }

  /**
   * Create an settings from JSON
   *
   * @param json the JSON
   */
  public DropRequestOptions(JsonObject json) {
    init();
    DropRequestOptionsConverter.fromJson(json, this);
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    DropRequestOptionsConverter.toJson(this, json);
    return json;
  }

  private void init() {
    enabled = DEFAULT_DROP_REQUESTS;
    dropRequestResponseCode = DEFAULT_DROP_REQUESTS_RESPONSE_CODE;
    backpressureBufferCapacity = DEFAULT_BACKPRESSURE_BUFFER_SIZE;
    backpressureStrategy = DEFAULT_BACKPRESSURE_STRATEGY;
  }

  /**
   * @return true if request dropping (backpressure) is enabled
   */
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * Enabled/disables request dropping (backpressure) on heavy load. Default is true - enabled.
   *
   * @param enabled true - request drop enabled, false if disabled
   * @return reference to this, so the API can be used fluently
   */
  public DropRequestOptions setEnabled(boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  /**
   * @return HTTP response code used when request dropped
   */
  public int getDropRequestResponseCode() {
    return dropRequestResponseCode;
  }

  /**
   * Sets the HTTP response code returned wheb request is dropped. Default is
   * TOO_MANY_REQUESTS(429)
   *
   * @param dropRequestResponseCode status code integer
   * @return reference to this, so the API can be used fluently
   */
  public DropRequestOptions setDropRequestResponseCode(int dropRequestResponseCode) {
    this.dropRequestResponseCode = dropRequestResponseCode;
    return this;
  }

  /**
   * @return Capacity of the backpressure buffer
   */
  public long getBackpressureBufferCapacity() {
    return backpressureBufferCapacity;
  }

  /**
   * Sets the backpressure buffer capacity, the number of request that single Server instance can
   * support concurrently. Default value is 1000.
   *
   * @param backpressureBufferCapacity long - capacity of the buffer
   * @return reference to this, so the API can be used fluently
   */
  public DropRequestOptions setBackpressureBufferCapacity(long backpressureBufferCapacity) {
    this.backpressureBufferCapacity = backpressureBufferCapacity;
    return this;
  }

  /**
   * @return a backpressure overflow strategy.
   */
  public BackpressureOverflowStrategy getBackpressureStrategy() {
    return backpressureStrategy;
  }

  /**
   * Sets the strategy how to deal with backpressure buffer overflow. Default is DROP_LATEST.
   *
   * Available values:
   * <ul>
   * <li>ERROR - terminates the whole sequence</li>
   * <li>DROP_OLDEST - drops the oldest value from the buffer</li>
   * <li>DROP_LATEST - drops the latest value from the buffer</li>
   * </ul>
   *
   * @param backpressureStrategy a BackpressureOverflowStrategy value
   * @return reference to this, so the API can be used fluently
   */
  public DropRequestOptions setBackpressureStrategy(
      BackpressureOverflowStrategy backpressureStrategy) {
    this.backpressureStrategy = backpressureStrategy;
    return this;
  }
}
