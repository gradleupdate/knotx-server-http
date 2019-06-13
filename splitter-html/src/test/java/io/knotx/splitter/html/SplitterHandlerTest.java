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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.knotx.fragment.Fragment;
import io.knotx.server.api.context.ClientRequest;
import io.knotx.server.api.context.ClientResponse;
import io.knotx.server.api.context.RequestEvent;
import io.knotx.server.api.handler.RequestEventHandlerResult;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ext.web.RoutingContext;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SplitterHandlerTest {

  @Mock
  private RequestEvent requestEvent;

  @Mock
  private ClientResponse clientResponse;

  @Mock
  private HtmlFragmentSplitter splitter;

  @Mock
  private RoutingContext routingContext;

  private SplitterHandler tested;

  @BeforeEach
  void setUp() {
    tested = new SplitterHandler(splitter);
  }

  @Test
  @DisplayName("Expect failed result when template body is missing")
  void handleMissingTemplate() {
    // given
    when(clientResponse.getBody()).thenReturn(null);

    // when
    final RequestEventHandlerResult result = tested
        .provideFragmentsAndClearBody(routingContext, requestEvent, clientResponse);

    // then
    assertEquals("Template body is missing!", result.getErrorMessage());
  }

  @Test
  @DisplayName("Expect failed result when template body is empty")
  void handleEmptyTemplate() {
    // given
    when(clientResponse.getBody()).thenReturn(Buffer.buffer());

    // when
    final RequestEventHandlerResult result = tested
        .provideFragmentsAndClearBody(routingContext, requestEvent, clientResponse);

    // then
    assertEquals("Template body is missing!", result.getErrorMessage());
  }

  @Test
  @DisplayName("Expect request event payload, fragments and client request in result when template is not empty")
  void checkPayloadAndClientRequestRewritten() {
    // given
    Buffer buffer = Mockito.mock(Buffer.class);
    when(buffer.toString()).thenReturn("body content");
    when(clientResponse.getBody()).thenReturn(buffer);

    ClientRequest clientRequest = Mockito.mock(ClientRequest.class);
    when(requestEvent.getClientRequest()).thenReturn(clientRequest);

    JsonObject payload = new JsonObject().put("secret", "12345");
    when(requestEvent.getPayload()).thenReturn(payload);

    List<Fragment> fragments = Collections.emptyList();
    when(splitter.split("body content")).thenReturn(fragments);

    // when
    final RequestEventHandlerResult result = tested
        .provideFragmentsAndClearBody(routingContext, requestEvent, clientResponse);

    // then
    assertTrue(result.getRequestEvent().isPresent());
    final RequestEvent requestEvent = result.getRequestEvent().get();
    verify(routingContext).put("fragments", fragments);
    assertSame(clientRequest, requestEvent.getClientRequest());
    assertEquals(payload, requestEvent.getPayload());
  }

  @Test
  @DisplayName("Expect client response body cleared when template is not empty")
  void checkBodyCleared() {
    // given
    Buffer buffer = Mockito.mock(Buffer.class);
    when(buffer.toString()).thenReturn("body content");
    when(clientResponse.getBody()).thenReturn(buffer);

    // when
    tested.provideFragmentsAndClearBody(routingContext, requestEvent, clientResponse);

    // then
    verify(clientResponse).setBody(null);
  }
}