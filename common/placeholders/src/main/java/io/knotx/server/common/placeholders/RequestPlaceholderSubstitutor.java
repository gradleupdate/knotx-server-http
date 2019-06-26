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
package io.knotx.server.common.placeholders;

import static io.knotx.server.common.placeholders.configuration.SourceDefinitions.PREFIX_REQUEST_HEADER;
import static io.knotx.server.common.placeholders.configuration.SourceDefinitions.PREFIX_REQUEST_PARAM;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import io.knotx.server.api.context.ClientRequest;

public class RequestPlaceholderSubstitutor implements PlaceholderSubstitutor<ClientRequest> {

  @Override
  public String getValue(final ClientRequest request, final String placeholder) {
    return Arrays.stream(Strategy.values())
        .filter(strategy -> StringUtils.startsWith(placeholder, strategy.prefix))
        .findFirst().map(strategy -> strategy.getValue(request, placeholder)).orElse(null);
  }

  private enum Strategy {

    HEADER(PREFIX_REQUEST_HEADER) {
      @Override
      String getValue(ClientRequest request, String placeholder) {
        return request.getHeaders().get(getName(placeholder));
      }
    },
    PARAM(PREFIX_REQUEST_PARAM) {
      @Override
      String getValue(ClientRequest request, String placeholder) {
        return request.getParams().get(getName(placeholder));
      }
    };

    final String prefix;

    Strategy(String prefix) {
      this.prefix = prefix;
    }

    String getName(String placeholder) {
      return StringUtils.substringAfter(placeholder, ".");
    }

    abstract String getValue(ClientRequest request, String placeholder);
  }

}
