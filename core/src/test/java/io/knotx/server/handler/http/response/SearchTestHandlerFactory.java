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
package io.knotx.server.handler.http.response;

import io.knotx.server.api.handler.RoutingHandlerFactory;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.RoutingContext;

public class SearchTestHandlerFactory implements RoutingHandlerFactory {

  private static final String SEARCH_RESPONSE = new JsonArray()
      .add(book(99999, "Book One"))
      .add(book(12345, "Book Two"))
      .toString();

  @Override
  public String getName() {
    return "test-search";
  }

  @Override
  public Handler<RoutingContext> create(Vertx vertx, JsonObject config) {
    return context -> context.response().end(SEARCH_RESPONSE);
  }

  private static JsonObject book(int id, String title) {
    return new JsonObject().put("id", id).put("title", title);
  }

}