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
package io.knotx.server.common.placeholders.configuration;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import io.knotx.server.api.context.ClientRequest;
import io.knotx.server.common.placeholders.JsonPlaceholderSubstitutor;
import io.knotx.server.common.placeholders.RequestPlaceholderSubstitutor;
import io.knotx.server.common.placeholders.UriPlaceholderSubstitutor;
import io.vertx.core.json.JsonObject;

public class PlaceholdersResolverConfiguration {

  private final Map<Class, PlaceholdersResolverConfigurationItem> configuration;

  private PlaceholdersResolverConfiguration(
      Map<Class, PlaceholdersResolverConfigurationItem> configuration) {
    this.configuration = configuration;
  }

  public PlaceholdersResolverConfigurationItem getItem(Class clazz) {
    return configuration.get(clazz);
  }

  public static PlaceholdersResolverConfiguration fromSourceDefinitions(
      SourceDefinitions sourceDefinitions) {
    ImmutableMap.Builder<Class, PlaceholdersResolverConfigurationItem> builder = ImmutableMap.builder();

    List<SourceDefinition> clientRequestSources = sourceDefinitions.getSourceDefinitionsByClass(
        ClientRequest.class);

    Preconditions.checkArgument(clientRequestSources.size() <= 1,
        "Maximum one Client Request source allowed");

    if (!clientRequestSources.isEmpty()) {
      builder.put(ClientRequest.class, initClientRequestSource(clientRequestSources.get(0)));
    }

    sourceDefinitions.getSourceDefinitionsByClass(JsonObject.class)
        .forEach(
            sourceDefinition -> builder.put(JsonObject.class,
                initJsonObjectSource(sourceDefinition)));

    return new PlaceholdersResolverConfiguration(builder.build());
  }

  private static PlaceholdersResolverConfigurationItem initJsonObjectSource(
      SourceDefinition<JsonObject> sourceDefinition) {
    return new PlaceholdersResolverConfigurationItem(
        Lists.newArrayList(new JsonPlaceholderSubstitutor()), sourceDefinition.getPrefixes());
  }

  private static PlaceholdersResolverConfigurationItem initClientRequestSource(
      SourceDefinition<ClientRequest> sourceDefinition) {
    return new PlaceholdersResolverConfigurationItem(
        newArrayList(new RequestPlaceholderSubstitutor(), new UriPlaceholderSubstitutor()),
        sourceDefinition.getPrefixes());
  }
}