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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.api.contract.RouterFactoryException;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.reactivex.core.Vertx;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
class MissingSecurityConfigTest {

  @Test
  @DisplayName("Expect failed server start because of missing security handler definition")
  void failBecauseOfMissingSecurityHandlerConfig(VertxTestContext testContext, Vertx vertx) {
    // given
    JsonObject configs = new JsonObject()
        .put("routingSpecificationLocation", "security/missing-security-handler.yaml")
        .put("routingOperations",
            new JsonArray().add(new JsonObject()
                .put("operationId", "listBooksWithApiKeyAuth")
            ));
    DeploymentOptions options = new DeploymentOptions().setConfig(configs);

    // when
    vertx.rxDeployVerticle(KnotxServerVerticle.class.getName(), options)
        .subscribe(
            // then
            success -> {
              //this should not happen
              testContext.failNow(new RuntimeException("This test should fail!"));
            },
            throwable -> {
              testContext.verify(() -> {
                assertTrue(throwable instanceof RouterFactoryException);
                assertEquals("Missing handler for security requirement: apiKeyAuthBooks", throwable.getMessage());
              });
              testContext.completeNow();
            }
        );
  }

}