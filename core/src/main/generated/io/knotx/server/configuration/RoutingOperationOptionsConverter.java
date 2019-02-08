package io.knotx.server.configuration;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Converter for {@link io.knotx.server.configuration.RoutingOperationOptions}.
 * NOTE: This class has been automatically generated from the {@link io.knotx.server.configuration.RoutingOperationOptions} original class using Vert.x codegen.
 */
 class RoutingOperationOptionsConverter {

   static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, RoutingOperationOptions obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "failureHandlers":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<io.knotx.server.configuration.RoutingHandlerOptions> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof JsonObject)
                list.add(new io.knotx.server.configuration.RoutingHandlerOptions((JsonObject)item));
            });
            obj.setFailureHandlers(list);
          }
          break;
        case "handlers":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<io.knotx.server.configuration.RoutingHandlerOptions> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof JsonObject)
                list.add(new io.knotx.server.configuration.RoutingHandlerOptions((JsonObject)item));
            });
            obj.setHandlers(list);
          }
          break;
        case "operationId":
          if (member.getValue() instanceof String) {
            obj.setOperationId((String)member.getValue());
          }
          break;
      }
    }
  }

   static void toJson(RoutingOperationOptions obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

   static void toJson(RoutingOperationOptions obj, java.util.Map<String, Object> json) {
    if (obj.getOperationId() != null) {
      json.put("operationId", obj.getOperationId());
    }
  }
}
