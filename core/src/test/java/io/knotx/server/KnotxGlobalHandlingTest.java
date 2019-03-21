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
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(KnotxExtension.class)
@KnotxApplyConfiguration({"server.conf", "server-random-port.conf", "global/global.conf"})
class KnotxGlobalHandlingTest {

  private static final String EXISTING_ENDPOINT_URL = "/test/any";

  @Test
  @DisplayName("Expect content type cookie in response when global contentTypeCookie handler is set")
  void callDefinedRoute(Vertx vertx, @RandomPort Integer globalServerPort) {
    // @formatter:off
    given().
        port(globalServerPort).
    when().
      get(EXISTING_ENDPOINT_URL).
    then().assertThat().
        statusCode(200).
        cookie("Content-Type", "global");
    // @formatter:on
  }

  @Test
  @DisplayName("Expect no cookies in response when calling undefined route and global contentTypeCookie handler is set")
  void callUndefinedRoute(Vertx vertx, @RandomPort Integer globalServerPort) {
    // @formatter:off
    given().
        port(globalServerPort).
    when().
        get("/undefined").
    then().assertThat().
        cookies(Collections.emptyMap());
    // @formatter:on
  }

}
