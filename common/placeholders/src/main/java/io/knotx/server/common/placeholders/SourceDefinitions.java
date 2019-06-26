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

import static com.google.common.collect.Sets.newHashSet;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import io.knotx.server.api.context.ClientRequest;
import io.vertx.core.json.JsonObject;

public class SourceDefinitions {

  static final String PREFIX_REQUEST_HEADER = "header";
  static final String PREFIX_REQUEST_PARAM = "param";

  static final String URI_PREFIX = "uri";
  static final String SLING_URI_PREFIX = "slingUri";

  private static final Set<String> CLIENT_REQUEST_PREFIXES = ImmutableSet.<String>builder()
      .add(SLING_URI_PREFIX, URI_PREFIX, PREFIX_REQUEST_HEADER, PREFIX_REQUEST_PARAM)
      .build();

  private static final Set<PlaceholderSubstitutor<ClientRequest>> CLIENT_REQUEST_SUBSTITUTOR = ImmutableSet.<PlaceholderSubstitutor<ClientRequest>>builder().add(
      new RequestPlaceholderSubstitutor(), new UriPlaceholderSubstitutor())
      .build();

  private final Set<SourceDefinition> definitions;

  private SourceDefinitions(
      Set<SourceDefinition> definitions) {
    this.definitions = ImmutableSet.copyOf(definitions);
  }

  Set<SourceDefinition> getSourceDefinitions() {
    return definitions;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private Set<SourceDefinition> sourceDefinitions;

    Builder() {
      this.sourceDefinitions = newHashSet();
    }

    public Builder addClientRequestSource(ClientRequest source) {
      SourceDefinition<ClientRequest> sourceDefinition = new SourceDefinition<>(
          source, CLIENT_REQUEST_PREFIXES, CLIENT_REQUEST_SUBSTITUTOR);
      sourceDefinitions.add(sourceDefinition);
      return this;
    }

    public Builder addJsonObjectSource(JsonObject source, String prefix) {
      SourceDefinition<JsonObject> sourceDefinition = new SourceDefinition<>(source,
          newHashSet(prefix), newHashSet(new JsonPlaceholderSubstitutor()));
      sourceDefinitions.add(sourceDefinition);
      return this;
    }

    public SourceDefinitions build() {
      return new SourceDefinitions(sourceDefinitions);
    }
  }
}