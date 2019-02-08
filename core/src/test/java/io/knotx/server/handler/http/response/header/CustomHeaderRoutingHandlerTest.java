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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import io.knotx.server.handler.http.response.header.CustomHeaderRoutingHandlerFactory.CustomHeaderRoutingHandler;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.MultiMap;
import io.vertx.reactivex.core.http.HttpServerResponse;
import io.vertx.reactivex.ext.web.RoutingContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomHeaderRoutingHandlerTest {

  @Mock
  private RoutingContext context;

  @Mock
  private HttpServerResponse response;

  private MultiMap headers;

  @BeforeEach
  void setUp() {
    headers = MultiMap.caseInsensitiveMultiMap();
  }

  @Test
  void handle_emptyConfiguration_expectNoCustomHeadersSet() {
    //when
    final CustomHeaderRoutingHandler handler = new CustomHeaderRoutingHandler(new JsonObject());
    handler.handle(context);

    //then
    assertTrue(headers.isEmpty());
  }

  @Test
  void handle_whenCustomHeaderConfigured_expectCustomHeaderSet() {
    when(context.response()).thenReturn(response);
    when(response.headers()).thenReturn(headers);

    //when
    final JsonObject config = new JsonObject()
        .put("name", "testName")
        .put("value", "testValue");
    final CustomHeaderRoutingHandler handler = new CustomHeaderRoutingHandler(config);
    handler.handle(context);

    //then
    assertTrue(headers.contains("testName"));
    assertEquals(headers.get("testName"), "testValue");
  }
}
