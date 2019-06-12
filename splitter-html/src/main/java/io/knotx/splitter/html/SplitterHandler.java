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

import io.knotx.fragment.Fragment;
import io.knotx.server.api.context.ClientResponse;
import io.knotx.server.api.context.RequestContext;
import io.knotx.server.api.context.RequestEvent;
import io.knotx.server.api.handler.DefaultRequestContextEngine;
import io.knotx.server.api.handler.RequestContextEngine;
import io.knotx.server.api.handler.RequestEventHandlerResult;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.ext.web.RoutingContext;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

public class SplitterHandler implements Handler<RoutingContext> {

  private static final Logger LOGGER = LoggerFactory.getLogger(SplitterHandler.class);
  private static final String MISSING_REPOSITORY_PAYLOAD = "Template body is missing!";

  private final RequestContextEngine engine;
  private final HtmlFragmentSplitter splitter;

  SplitterHandler() {
    engine = new DefaultRequestContextEngine(getClass().getSimpleName());
    splitter = new HtmlFragmentSplitter();
  }

  //for unit tests
  SplitterHandler(HtmlFragmentSplitter splitter) {
    engine = new DefaultRequestContextEngine(getClass().getSimpleName());
    this.splitter = splitter;
  }

  @Override
  public void handle(RoutingContext context) {
    RequestContext requestContext = context.get(RequestContext.KEY);
    try {
      ClientResponse clientResponse = requestContext.getClientResponse();
      RequestEvent requestEvent = requestContext.getRequestEvent();

      final RequestEventHandlerResult result = provideFragmentsAndClearBody(context,
          requestEvent, clientResponse);

      engine.processAndSaveResult(result, context, requestContext);
    } catch (Exception e) {
      engine.handleFatal(context, requestContext, e);
    }
  }

  RequestEventHandlerResult provideFragmentsAndClearBody(RoutingContext context,
      RequestEvent requestEvent, ClientResponse clientResponse) {
    final RequestEventHandlerResult result;
    final String template = Optional.ofNullable(clientResponse.getBody()).map(Buffer::toString)
        .orElse(null);
    if (StringUtils.isNotBlank(template)) {
      splitBodyAndPutFragmentsInTheContext(context, template);
      clearBody(clientResponse);
      result = RequestEventHandlerResult.success(requestEvent);
    } else {
      LOGGER.error(MISSING_REPOSITORY_PAYLOAD);
      result = RequestEventHandlerResult.fail(MISSING_REPOSITORY_PAYLOAD);
    }
    return result;
  }

  private void clearBody(ClientResponse clientResponse) {
    clientResponse.setBody(null);
  }

  private void splitBodyAndPutFragmentsInTheContext(RoutingContext context, String template) {
    List<Fragment> fragments = splitter.split(template);
    context.put("fragments", fragments);
  }

}
