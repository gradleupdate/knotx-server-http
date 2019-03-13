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
import io.vertx.reactivex.core.Vertx;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Tests are created according to Open API 3.0 Spec: https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.2.md
 */
@ExtendWith(KnotxExtension.class)
@KnotxApplyConfiguration({"server.conf", "server-random-port.conf"})
class KnotxServerValidationTest {

  private static final String SERVER_URL = "/test/any";

  @Disabled
  @Test
  @DisplayName("Expect OK when request parameters validation passes.")
  void automaticRequestParamsValidationSuccess(Vertx vertx, @RandomPort Integer globalServerPort) {
    // see example here: https://github.com/OAI/OpenAPI-Specification/blob/master/examples/v3.0/petstore-expanded.yaml
  }

  @Disabled
  @Test
  @DisplayName("Expect BAD REQUEST when request parameters validation fails.")
  void automaticRequestParamsValidationFailure(Vertx vertx, @RandomPort Integer globalServerPort) {
    // see example here: https://github.com/OAI/OpenAPI-Specification/blob/master/examples/v3.0/petstore-expanded.yaml
  }

  @Disabled
  @Test
  @DisplayName("Expect OK when response schema is valid.")
  void automaticResponseSchemaValidationSuccess(Vertx vertx, @RandomPort Integer globalServerPort) {
    // see example here: https://github.com/OAI/OpenAPI-Specification/blob/master/examples/v3.0/petstore-expanded.yaml
  }

  @Disabled
  @Test
  @DisplayName("Expect SERVER ERROR when response schema is invalid.")
  void automaticResponseSchemaValidationFailure(Vertx vertx, @RandomPort Integer globalServerPort) {
    // see example here: https://github.com/OAI/OpenAPI-Specification/blob/master/examples/v3.0/petstore-expanded.yaml
  }

}
