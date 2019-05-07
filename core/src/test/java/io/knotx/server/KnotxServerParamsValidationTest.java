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

import static io.restassured.RestAssured.given;

import io.knotx.junit5.KnotxApplyConfiguration;
import io.knotx.junit5.KnotxExtension;
import io.knotx.junit5.RandomPort;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.JsonArray;
import io.vertx.reactivex.core.Vertx;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Tests are created according to Open API 3.0 Spec: https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.2.md
 */
@ExtendWith(KnotxExtension.class)
@KnotxApplyConfiguration({"server.conf", "server-random-port.conf", "params-validation/parameters.conf"})
class KnotxServerParamsValidationTest {

  @Test
  @DisplayName("Expect OK when request parameters validation passes.")
  void automaticRequestParamsValidationSuccess(Vertx vertx, @RandomPort Integer globalServerPort) {
    given().
        port(globalServerPort).
    when().
        get("/query-params?q=knotx&limit=5").
    then().assertThat().
        statusCode(HttpResponseStatus.OK.code());
  }

  @Test
  @DisplayName("Expect BAD REQUEST when request parameters validation fails.")
  void automaticRequestParamsValidationFailure(Vertx vertx, @RandomPort Integer globalServerPort) {
    given().
        port(globalServerPort).
    when().
        get("/query-params?q=knotx&limit=five").
    then().assertThat().
        statusCode(HttpResponseStatus.BAD_REQUEST.code());
  }

  @Disabled
  @Test
  @DisplayName("Expect OK when path parameters validation passes.")
  void automaticPathParamsValidationSuccess(Vertx vertx, @RandomPort Integer globalServerPort) {
    given().
        port(globalServerPort).
    when().
        get("/path-params/knotx/5").
    then().assertThat().
        statusCode(HttpResponseStatus.OK.code());
  }

  @Disabled
  @Test
  @DisplayName("Expect BAD REQUEST when path parameters validation fails.")
  void automaticPathParamsValidationFailure(Vertx vertx, @RandomPort Integer globalServerPort) {
    given().
        port(globalServerPort).
    when().
        get("/path-params/knotx/five").
    then().assertThat().
        statusCode(HttpResponseStatus.BAD_REQUEST.code());
  }

}
