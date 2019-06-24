package io.knotx.server.common.placeholders;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import io.knotx.server.api.context.ClientRequest;
import io.vertx.core.json.JsonObject;

class PlaceholdersResolverConfiguration {

  private final Map<Class, PlaceholdersResolverConfigurationItem> configuration;

  PlaceholdersResolverConfiguration() {
    configuration = init();
  }

  private Map<Class, PlaceholdersResolverConfigurationItem> init() {
    Map map = new HashMap();
    map.put(ClientRequest.class, initClientRequetSource());
    map.put(JsonObject.class, initJsonObjectSource());

    return map;
  }

  private PlaceholdersResolverConfigurationItem initJsonObjectSource() {
    return new PlaceholdersResolverConfigurationItem(
        Arrays.asList(new JsonPlaceholderSubstitutor()),
        Arrays.asList(JsonPlaceholderSubstitutor.PREFIX_PAYLOAD));
  }

  private PlaceholdersResolverConfigurationItem initClientRequetSource() {
    return new PlaceholdersResolverConfigurationItem(
        Arrays.asList(new RequestPlaceholderSubstitutor(), new UriPlaceholderSubstitutor()),
        Arrays.asList(UriPlaceholderSubstitutor.SLING_URI_PREFIX,
            UriPlaceholderSubstitutor.URI_PREFIX, RequestPlaceholderSubstitutor.PREFIX_HEADER,
            RequestPlaceholderSubstitutor.PREFIX_PARAM));
  }

  PlaceholdersResolverConfigurationItem getItem(Class clazz) {
    return configuration.get(clazz);
  }
}