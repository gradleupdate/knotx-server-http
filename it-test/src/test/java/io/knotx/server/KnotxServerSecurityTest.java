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
@Disabled
@ExtendWith(KnotxExtension.class)
@KnotxApplyConfiguration("server.conf")
class KnotxServerSecurityTest {

  private static final String BASIC_AUTH_ENDPOINT_URL = "/test/securedBasic";
  private static final String API_KEY_ENDPOINT_URL = "/test/securedApiKey";
  private static final String JWT_ENDPOINT_URL = "/test/securedJwt";
  private static final String OAUTH_ENDPOINT_URL = "/test/securedOAuth";

  @Test
  @DisplayName("Expect Ok when basic authentication passes.")
  void basicAuthenticationSuccess(Vertx vertx, @RandomPort Integer globalServerPort) {
    // @formatter:off
    given().
        port(globalServerPort).
    //  header(BASIC_AUTH)
    when().
      get(BASIC_AUTH_ENDPOINT_URL).
    then().assertThat().
        statusCode(200);
    // @formatter:on
  }

  @Test
  @DisplayName("Expect Unauthorized when basic authentication fails.")
  void basicAuthenticationFailure(Vertx vertx, @RandomPort Integer globalServerPort) {
    // @formatter:off
    given().
        port(globalServerPort).
    when().
      get(BASIC_AUTH_ENDPOINT_URL).
    then().assertThat().
        statusCode(401);
    // @formatter:on
  }

  @Test
  @DisplayName("Expect Ok when API key authentication passes.")
  void apiKeyAuthenticationSuccess(Vertx vertx, @RandomPort Integer globalServerPort) {
    // @formatter:off
    given().
        port(globalServerPort).
    //  header(API_KEY)
    when().
      get(API_KEY_ENDPOINT_URL).
    then().assertThat().
        statusCode(200);
    // @formatter:on
  }

  @Test
  @DisplayName("Expect Unauthorized when API key authentication fails.")
  void apiKeyAuthenticationFailure(Vertx vertx, @RandomPort Integer globalServerPort) {
    // @formatter:off
    given().
        port(globalServerPort).
    when().
      get(API_KEY_ENDPOINT_URL).
    then().assertThat().
        statusCode(401);
    // @formatter:on
  }

  @Test
  @DisplayName("Expect Ok when JWT authentication passes.")
  void jwtAuthenticationSuccess(Vertx vertx, @RandomPort Integer globalServerPort) {
    // @formatter:off
    given().
        port(globalServerPort).
    //  header(JWT Bearer)
    when().
      get(JWT_ENDPOINT_URL).
    then().assertThat().
        statusCode(200);
    // @formatter:on
  }

  @Test
  @DisplayName("Expect Unauthorized when JWT authentication fails.")
  void jwtAuthenticationFailure(Vertx vertx, @RandomPort Integer globalServerPort) {
    // @formatter:off
    given().
        port(globalServerPort).
    when().
      get(JWT_ENDPOINT_URL).
    then().assertThat().
        statusCode(401);
    // @formatter:on
  }

  @Test
  @DisplayName("Expect Ok when OAuth 2.0 authentication passes.")
  void oAuthAuthenticationSuccess(Vertx vertx, @RandomPort Integer globalServerPort) {
    // @formatter:off
    given().
        port(globalServerPort).
    //  header(OAuth)
    when().
      get(OAUTH_ENDPOINT_URL).
    then().assertThat().
        statusCode(200);
    // @formatter:on
  }

  @Test
  @DisplayName("Expect Unauthorized when OAuth 2.0 authentication fails.")
  void oAuthAuthenticationFailure(Vertx vertx, @RandomPort Integer globalServerPort) {
    // @formatter:off
    given().
        port(globalServerPort).
    when().
      get(OAUTH_ENDPOINT_URL).
    then().assertThat().
        statusCode(401);
    // @formatter:on
  }

}
