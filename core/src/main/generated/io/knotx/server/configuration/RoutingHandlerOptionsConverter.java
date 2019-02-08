package io.knotx.server.configuration;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Converter for {@link io.knotx.server.configuration.RoutingHandlerOptions}.
 * NOTE: This class has been automatically generated from the {@link io.knotx.server.configuration.RoutingHandlerOptions} original class using Vert.x codegen.
 */
 class RoutingHandlerOptionsConverter {

   static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, RoutingHandlerOptions obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "config":
          if (member.getValue() instanceof JsonObject) {
            obj.setConfig(((JsonObject)member.getValue()).copy());
          }
          break;
        case "name":
          if (member.getValue() instanceof String) {
            obj.setName((String)member.getValue());
          }
          break;
      }
    }
  }

   static void toJson(RoutingHandlerOptions obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

   static void toJson(RoutingHandlerOptions obj, java.util.Map<String, Object> json) {
    if (obj.getConfig() != null) {
      json.put("config", obj.getConfig());
    }
    if (obj.getName() != null) {
      json.put("name", obj.getName());
    }
  }
}
