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
package io.knotx.server.handler.http.response.writer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.knotx.server.api.context.ClientResponse;
import io.knotx.server.api.context.RequestContext;
import io.knotx.server.api.context.RequestContext.Status;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpHeaders;
import io.vertx.reactivex.core.MultiMap;
import io.vertx.reactivex.core.http.HttpServerResponse;
import java.util.Collections;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ServerResponseTest {

  private static final Set<String> DEFAULT_ALLOWED_HEADERS = Collections.singleton("test");

  @Mock
  private HttpServerResponse httpResponse;

  @Mock
  private RequestContext requestContext;

  @Mock
  private ClientResponse clientResponse;

  @Mock
  private Status status;

  private MultiMap headersMultiMap;

  @BeforeEach
  void setUp() {
    when(requestContext.getClientResponse()).thenReturn(clientResponse);
    when(requestContext.getStatus()).thenReturn(status);
    headersMultiMap = MultiMap.caseInsensitiveMultiMap();
  }

  @Test
  @DisplayName("Expect 500 when context failed and no status code set in client response")
  void endFailedContextNoStatusCodeSet() {
    //given
    when(status.isFailed()).thenReturn(true);
    when(clientResponse.getStatusCode()).thenReturn(0);

    //when
    new ServerResponse(requestContext).end(httpResponse, DEFAULT_ALLOWED_HEADERS);

    //then
    verify(httpResponse).setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
    verify(httpResponse).end();
  }

  @Test
  @DisplayName("Expect status code rewritten from client response when context failed")
  void endFailedContextStatusCodeSet() {
    //given
    when(status.isFailed()).thenReturn(true);
    final int expectedStatusCode = HttpResponseStatus.NOT_FOUND.code();
    when(clientResponse.getStatusCode()).thenReturn(expectedStatusCode);

    //when
    new ServerResponse(requestContext).end(httpResponse, DEFAULT_ALLOWED_HEADERS);

    //then
    verify(httpResponse).setStatusCode(expectedStatusCode);
    verify(httpResponse).end();
  }

  @Test
  @DisplayName("Expect status code, body and headers rewritten from client response when context ok")
  void endOkContextStatusCodeSet() {
    //given
    when(status.isFailed()).thenReturn(false);
    when(httpResponse.headers()).thenReturn(headersMultiMap);
    final int expectedStatusCode = HttpResponseStatus.NOT_FOUND.code();
    when(clientResponse.getStatusCode()).thenReturn(expectedStatusCode);
    final Buffer body = Buffer.buffer("template not found");
    when(clientResponse.getBody()).thenReturn(body);
    when(clientResponse.getHeaders())
        .thenReturn(MultiMap.caseInsensitiveMultiMap().add("test", "testValue"));

    //when
    new ServerResponse(requestContext).end(httpResponse, DEFAULT_ALLOWED_HEADERS);

    //then
    verify(httpResponse).setStatusCode(expectedStatusCode);
    assertEquals(headersMultiMap.get("test"), "testValue");
    verify(httpResponse).end(io.vertx.reactivex.core.buffer.Buffer.newInstance(body));
  }

  @Test
  @DisplayName("Expect empty body, status code and headers rewritten when context ok but no body set")
  void rewriteStatusWhenNoBodySet() {
    //given
    when(status.isFailed()).thenReturn(false);
    when(httpResponse.headers()).thenReturn(headersMultiMap);
    final int expectedStatusCode = HttpResponseStatus.OK.code();
    when(clientResponse.getStatusCode()).thenReturn(expectedStatusCode);
    when(clientResponse.getHeaders())
        .thenReturn(MultiMap.caseInsensitiveMultiMap().add("test", "testValue"));

    //when
    new ServerResponse(requestContext).end(httpResponse, DEFAULT_ALLOWED_HEADERS);

    //then
    verify(httpResponse).setStatusCode(expectedStatusCode);
    assertEquals(headersMultiMap.get("test"), "testValue");
    verify(httpResponse).end();
  }

  @Test
  @DisplayName("Expect content-length header removed when context ok")
  void checkContentLengthRemoved() {
    //given
    when(status.isFailed()).thenReturn(false);
    MultiMap headersMock = Mockito.mock(MultiMap.class);
    when(httpResponse.headers()).thenReturn(headersMock);
    final int expectedStatusCode = HttpResponseStatus.OK.code();
    when(clientResponse.getStatusCode()).thenReturn(expectedStatusCode);
    when(clientResponse.getHeaders())
        .thenReturn(MultiMap.caseInsensitiveMultiMap().add("test", "testValue"));

    //when
    new ServerResponse(requestContext).end(httpResponse, DEFAULT_ALLOWED_HEADERS);

    //then
    verify(headersMock).remove(HttpHeaders.CONTENT_LENGTH.toString());
  }

  @Test
  @DisplayName("Expect headers filtered out context ok")
  void checkHeadersFiltered() {
    //given
    when(status.isFailed()).thenReturn(false);
    when(httpResponse.headers()).thenReturn(headersMultiMap);
    when(clientResponse.getStatusCode()).thenReturn(HttpResponseStatus.OK.code());
    final MultiMap headers = MultiMap.caseInsensitiveMultiMap()
        .add("test", "testValue")
        .add("notAllowed", "value")
        .add("shouldBeFilteredOut", "value");
    when(clientResponse.getHeaders()).thenReturn(headers);

    //when
    new ServerResponse(requestContext).end(httpResponse, DEFAULT_ALLOWED_HEADERS);

    //then
    assertEquals(1, headersMultiMap.size());
    assertEquals(headersMultiMap.get("test"), "testValue");
  }
}