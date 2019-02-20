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
package io.knotx.splitter.html;

import io.knotx.server.api.context.RequestContext;
import io.knotx.server.api.handler.DefaultRequestContextEngine;
import io.knotx.server.api.handler.RequestContextEngine;
import io.knotx.server.api.handler.RequestEventHandlerResult;
import io.vertx.core.Handler;
import io.vertx.reactivex.ext.web.RoutingContext;

public class SplitterHandler implements Handler<RoutingContext> {

  private final RequestContextEngine engine;
  private final ClientResponseBodyExtractor extractor;

  SplitterHandler() {
    engine = new DefaultRequestContextEngine(getClass().getSimpleName());
    extractor = new ClientResponseBodyExtractor();
  }

  @Override
  public void handle(RoutingContext context) {
    RequestContext requestContext = context.get(RequestContext.KEY);
    try {
      RequestEventHandlerResult result = extractor
          .splitAndClearBody(requestContext.getRequestEvent(), requestContext.getClientResponse());
      engine.processAndSaveResult(result, context, requestContext);
    } catch (Exception e) {
      engine.handleFatal(context, requestContext, e);
    }
  }

}
