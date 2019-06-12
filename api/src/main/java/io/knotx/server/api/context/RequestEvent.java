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
package io.knotx.server.api.context;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * Contains information about currently processed request event.
 */
@DataObject
public class RequestEvent {

  private static final String CLIENT_REQUEST_KEY = "clientRequest";
  private static final String PAYLOAD_KEY = "payload";

  private final ClientRequest clientRequest;
  private final JsonObject payload;

  public RequestEvent(ClientRequest clientRequest, JsonObject payload) {
    this.clientRequest = clientRequest;
    this.payload = payload;
  }

  public RequestEvent(ClientRequest clientRequest) {
    this.clientRequest = clientRequest;
    this.payload = new JsonObject();
  }

  public RequestEvent(JsonObject json) {
    this.clientRequest = new ClientRequest(json.getJsonObject(CLIENT_REQUEST_KEY));
    this.payload = json.getJsonObject(PAYLOAD_KEY);
  }

  /**
   * Currently processed {@code ClientRequest} details.
   * @return {@link ClientRequest} details.
   */
  public ClientRequest getClientRequest() {
    return clientRequest;
  }

  /**
   * A {@code JsonObject} that contains payload of currently processed request. It may contain any
   * additional data that is added during processing of the Request.
   *
   * @return copy of the payload
   */
  public JsonObject getPayload() {
    return payload.copy();
  }

  public JsonObject appendPayload(String key, Object value) {
    this.payload.put(key, value);
    return payload;
  }

  public JsonObject toJson() {
    return new JsonObject()
        .put(CLIENT_REQUEST_KEY, clientRequest.toJson())
        .put(PAYLOAD_KEY, payload);
  }

  @Override
  public String toString() {
    return "RequestEvent{" +
        "clientRequest=" + clientRequest +
        ", payload=" + payload +
        '}';
  }
}
