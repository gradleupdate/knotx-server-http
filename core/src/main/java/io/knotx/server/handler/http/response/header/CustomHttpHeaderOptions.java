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
package io.knotx.server.handler.http.response.header;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * Describes a custom header that is send with every HTTP response from the server
 */
@DataObject(generateConverter = true)
public class CustomHttpHeaderOptions {

  private String name;
  private String value;

  /**
   * Default Constructor
   */
  public CustomHttpHeaderOptions() {
    //Nothing to do
  }

  /**
   * Default copy constructor
   *
   * @param other the customHeader configuration to copy
   */
  public CustomHttpHeaderOptions(CustomHttpHeaderOptions other) {
    this.name = other.name;
    this.value = other.value;
  }

  /**
   * Creates from JSON
   *
   * @param json the JSON
   */
  public CustomHttpHeaderOptions(JsonObject json) {
    CustomHttpHeaderOptionsConverter.fromJson(json, this);
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    CustomHttpHeaderOptionsConverter.toJson(this, json);
    return json;
  }


  /**
   * Name of the request header.
   *
   * @param name name of the custom header
   * @return a reference to this, so the API can be used fluently
   */
  public CustomHttpHeaderOptions setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Value of the request header.
   *
   * @param value value of the custom header
   * @return a reference to this, so the API can be used fluently
   */
  public CustomHttpHeaderOptions setValue(String value) {
    this.value = value;
    return this;
  }


  /**
   * @return a request header name
   */
  public String getName() {
    return name;
  }

  /**
   * @return a request header value
   */
  public String getValue() {
    return value;
  }
}
