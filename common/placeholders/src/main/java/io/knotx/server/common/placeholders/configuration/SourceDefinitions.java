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

import static com.google.common.collect.Sets.newHashSet;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import io.knotx.server.api.context.ClientRequest;
import io.vertx.core.json.JsonObject;

public class SourceDefinitions {

  public static final String PREFIX_REQUEST_HEADER = "header";
  public static final String PREFIX_REQUEST_PARAM = "param";

  public static final String URI_PREFIX = "uri";
  public static final String SLING_URI_PREFIX = "slingUri";

  private final Set<SourceDefinition> sourceDefinitions;

  private SourceDefinitions(
      Set<SourceDefinition> sourceDefinitions) {
    this.sourceDefinitions = ImmutableSet.copyOf(sourceDefinitions);
  }

  public Set<SourceDefinition> getSourceDefinitions() {
    return sourceDefinitions;
  }

  <T> List<SourceDefinition> getSourceDefinitionsByClass(Class<T> clazz) {
    return sourceDefinitions.stream()
        .filter(sourceDefinition -> sourceDefinition.getSourceClass()
            .equals(clazz))
        .collect(
            toList());

  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private Set<SourceDefinition> sourceDefinitions;

    public Builder() {
      this.sourceDefinitions = Sets.newHashSet();
    }

    public Builder addClientRequestSource(ClientRequest source) {
      SourceDefinition<ClientRequest> sourceDefinition = new SourceDefinition<>(ClientRequest.class,
          source,
          getClientRequestPrefixes());
      sourceDefinitions.add(sourceDefinition);
      return this;
    }

    public Builder addJsonObjectSource(JsonObject source, String prefix) {
      SourceDefinition<JsonObject> sourceDefinition = new SourceDefinition<>(JsonObject.class,
          source, Sets.newHashSet(prefix));
      sourceDefinitions.add(sourceDefinition);
      return this;
    }

    public SourceDefinitions build() {
      return new SourceDefinitions(sourceDefinitions);
    }

    private Set<String> getClientRequestPrefixes() {
      return newHashSet(SLING_URI_PREFIX, URI_PREFIX, PREFIX_REQUEST_HEADER, PREFIX_REQUEST_PARAM);
    }
  }
}