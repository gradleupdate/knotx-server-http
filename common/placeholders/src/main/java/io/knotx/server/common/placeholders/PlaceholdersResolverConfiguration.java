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

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import io.knotx.server.api.context.ClientRequest;
import io.vertx.core.json.JsonObject;

class PlaceholdersResolverConfiguration {

  private final Map<Class, PlaceholdersResolverConfigurationItem> configuration;

  PlaceholdersResolverConfiguration() {
    configuration = init();
  }

  private Map<Class, PlaceholdersResolverConfigurationItem> init() {
    return ImmutableMap.<Class, PlaceholdersResolverConfigurationItem>builder()
        .put(ClientRequest.class, initClientRequestSource())
        .put(JsonObject.class, initJsonObjectSource())
        .build();
  }

  private PlaceholdersResolverConfigurationItem initJsonObjectSource() {
    return new PlaceholdersResolverConfigurationItem(
        newArrayList(new JsonPlaceholderSubstitutor()),
        newHashSet((JsonPlaceholderSubstitutor.PREFIX_PAYLOAD)));
  }

  private PlaceholdersResolverConfigurationItem initClientRequestSource() {
    return new PlaceholdersResolverConfigurationItem(
        newArrayList(new RequestPlaceholderSubstitutor(), new UriPlaceholderSubstitutor()),
        newHashSet(UriPlaceholderSubstitutor.SLING_URI_PREFIX,
            UriPlaceholderSubstitutor.URI_PREFIX, RequestPlaceholderSubstitutor.PREFIX_HEADER,
            RequestPlaceholderSubstitutor.PREFIX_PARAM));
  }

  PlaceholdersResolverConfigurationItem getItem(Class clazz) {
    return configuration.get(clazz);
  }
}