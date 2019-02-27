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
package io.knotx.server.api.handler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.knotx.server.api.context.ClientRequest;
import io.knotx.server.api.context.RequestContext;
import io.knotx.server.api.context.RequestEvent;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.buffer.Buffer;
import io.vertx.reactivex.core.MultiMap;
import io.vertx.reactivex.ext.web.RoutingContext;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DefaultRequestContextEngineTest {

  private static final String TEST_HANDLER_ID = "testId";

  @Mock
  private RequestEventHandlerResult result;

  @Mock
  private RoutingContext routingContext;

  @Mock
  private RequestContext requestContext;

  @Mock
  private RequestEvent requestEvent;

  private DefaultRequestContextEngine tested;


  @BeforeEach
  void setUp() {
    tested = new DefaultRequestContextEngine(TEST_HANDLER_ID);
  }

  @Test
  @DisplayName("Expect RequestContext updated and saved into RoutingContext when result failed")
  void updateRequestContextWhenFailedNoStatusCode() {
    //given
    setUpRequestContextUpdate();
    when(result.getRequestEvent()).thenReturn(Optional.empty());
    Buffer expectedBody = Buffer.buffer();
    MultiMap expectedHeaders = MultiMap.caseInsensitiveMultiMap().add("headerKey", "headerValue");
    int expectedStatusCode = HttpResponseStatus.INTERNAL_SERVER_ERROR.code();
    when(result.getBody()).thenReturn(expectedBody);
    when(result.getHeaders()).thenReturn(expectedHeaders);
    when(result.getStatusCode()).thenReturn(expectedStatusCode);

    //when
    tested.processAndSaveResult(result, routingContext, requestContext);

    //then
    verify(requestContext).setBody(expectedBody);
    verify(requestContext).addHeaders(expectedHeaders);
    verify(requestContext).setStatusCode(expectedStatusCode);
    verify(routingContext).put(RequestContext.KEY, requestContext);
  }

  @Test
  @DisplayName("Expect RequestContext updated and saved into RoutingContext when result succeed")
  void updateRequestContextWhenSuccess() {
    //given
    setUpRequestContextUpdate();
    when(result.getRequestEvent()).thenReturn(Optional.of(requestEvent));
    Buffer expectedBody = Buffer.buffer();
    MultiMap expectedHeaders = MultiMap.caseInsensitiveMultiMap().add("headerKey", "headerValue");
    int expectedStatusCode = HttpResponseStatus.OK.code();
    when(result.getBody()).thenReturn(expectedBody);
    when(result.getHeaders()).thenReturn(expectedHeaders);
    when(result.getStatusCode()).thenReturn(expectedStatusCode);

    //when
    tested.processAndSaveResult(result, routingContext, requestContext);

    //then
    verify(requestContext).setBody(expectedBody);
    verify(requestContext).addHeaders(expectedHeaders);
    verify(requestContext).setStatusCode(expectedStatusCode);
    verify(routingContext).put(RequestContext.KEY, requestContext);
  }

  @Test
  @DisplayName("Expect failure marked in RequestContext and failure handler called with status code from result when result failed")
  void failWhenFailureAndStatusCodeSet() {
    //given
    setUpRequestContextUpdate();
    when(result.getRequestEvent()).thenReturn(Optional.empty());
    int expectedStatusCode = HttpResponseStatus.NOT_FOUND.code();
    String expectedErrorMessage = "Test Error";
    when(result.getStatusCode()).thenReturn(expectedStatusCode);
    when(result.getErrorMessage()).thenReturn(expectedErrorMessage);

    //when
    tested.processAndSaveResult(result, routingContext, requestContext);

    //then
    verify(requestContext).failure(TEST_HANDLER_ID, expectedErrorMessage);
    verify(routingContext).fail(expectedStatusCode);
  }

  @Test
  @DisplayName("Expect failure marked in RequestContext and failure handler called with status code 500 when result failed with no status code")
  void failWhenFailureNoStatusCode() {
    //given
    setUpRequestContextUpdate();
    when(result.getRequestEvent()).thenReturn(Optional.empty());
    String expectedErrorMessage = "Test Error";
    when(result.getErrorMessage()).thenReturn(expectedErrorMessage);
    when(result.getStatusCode()).thenReturn(null);


    //when
    tested.processAndSaveResult(result, routingContext, requestContext);

    //then
    verify(requestContext).failure(TEST_HANDLER_ID, expectedErrorMessage);
    verify(routingContext).fail(HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
  }

  @Test
  @DisplayName("Expect success marked in RequestContext and next handler called when result succeed")
  void nextWhenSuccess() {
    //given
    setUpRequestContextUpdate();
    when(result.getRequestEvent()).thenReturn(Optional.of(requestEvent));

    //when
    tested.processAndSaveResult(result, routingContext, requestContext);

    //then
    verify(requestContext).success(TEST_HANDLER_ID, requestEvent);
    verify(routingContext).next();
  }

  @Test
  @DisplayName("Expect failure marked in RequestContext and failure handler called when fatal")
  void handleFatal() {
    //given
    when(requestContext.getRequestEvent()).thenReturn(requestEvent);
    ClientRequest clientRequest = Mockito.mock(ClientRequest.class);
    when(requestEvent.getClientRequest()).thenReturn(clientRequest);

    //when
    tested.handleFatal(routingContext, requestContext, null);

    //then
    verify(requestContext).fatal(TEST_HANDLER_ID);
    verify(routingContext).fail(HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
  }

  private void setUpRequestContextUpdate() {
    when(requestContext.setBody(any())).thenReturn(requestContext);
    when(requestContext.addHeaders(any())).thenReturn(requestContext);
    when(requestContext.setStatusCode(any())).thenReturn(requestContext);
  }
}