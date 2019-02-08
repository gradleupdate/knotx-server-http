package io.knotx.server.configuration;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Converter for {@link io.knotx.server.configuration.KnotxServerOptions}.
 * NOTE: This class has been automatically generated from the {@link io.knotx.server.configuration.KnotxServerOptions} original class using Vert.x codegen.
 */
 class KnotxServerOptionsConverter {

   static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, KnotxServerOptions obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "accessLog":
          if (member.getValue() instanceof JsonObject) {
            obj.setAccessLog(new io.knotx.server.handler.logger.AccessLogOptions((JsonObject)member.getValue()));
          }
          break;
        case "allowedResponseHeaders":
          if (member.getValue() instanceof JsonArray) {
            java.util.LinkedHashSet<java.lang.String> list =  new java.util.LinkedHashSet<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof String)
                list.add((String)item);
            });
            obj.setAllowedResponseHeaders(list);
          }
          break;
        case "backpressureBufferCapacity":
          if (member.getValue() instanceof Number) {
            obj.setBackpressureBufferCapacity(((Number)member.getValue()).longValue());
          }
          break;
        case "backpressureStrategy":
          if (member.getValue() instanceof String) {
            obj.setBackpressureStrategy(io.reactivex.BackpressureOverflowStrategy.valueOf((String)member.getValue()));
          }
          break;
        case "customResponseHeader":
          if (member.getValue() instanceof JsonObject) {
            obj.setCustomResponseHeader(new io.knotx.server.api.header.CustomHttpHeader((JsonObject)member.getValue()));
          }
          break;
        case "deliveryOptions":
          if (member.getValue() instanceof JsonObject) {
            obj.setDeliveryOptions(new io.vertx.core.eventbus.DeliveryOptions((JsonObject)member.getValue()));
          }
          break;
        case "displayExceptionDetails":
          if (member.getValue() instanceof Boolean) {
            obj.setDisplayExceptionDetails((Boolean)member.getValue());
          }
          break;
        case "dropRequestResponseCode":
          if (member.getValue() instanceof Number) {
            obj.setDropRequestResponseCode(((Number)member.getValue()).intValue());
          }
          break;
        case "dropRequests":
          if (member.getValue() instanceof Boolean) {
            obj.setDropRequests((Boolean)member.getValue());
          }
          break;
        case "routingOperations":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<io.knotx.server.configuration.RoutingOperationOptions> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof JsonObject)
                list.add(new io.knotx.server.configuration.RoutingOperationOptions((JsonObject)item));
            });
            obj.setRoutingOperations(list);
          }
          break;
        case "routingSpecificationLocation":
          if (member.getValue() instanceof String) {
            obj.setRoutingSpecificationLocation((String)member.getValue());
          }
          break;
        case "serverOptions":
          if (member.getValue() instanceof JsonObject) {
            obj.setServerOptions(new io.vertx.core.http.HttpServerOptions((JsonObject)member.getValue()));
          }
          break;
      }
    }
  }

   static void toJson(KnotxServerOptions obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

   static void toJson(KnotxServerOptions obj, java.util.Map<String, Object> json) {
    if (obj.getAccessLog() != null) {
      json.put("accessLog", obj.getAccessLog().toJson());
    }
    if (obj.getAllowedResponseHeaders() != null) {
      JsonArray array = new JsonArray();
      obj.getAllowedResponseHeaders().forEach(item -> array.add(item));
      json.put("allowedResponseHeaders", array);
    }
    json.put("backpressureBufferCapacity", obj.getBackpressureBufferCapacity());
    if (obj.getBackpressureStrategy() != null) {
      json.put("backpressureStrategy", obj.getBackpressureStrategy().name());
    }
    if (obj.getCustomResponseHeader() != null) {
      json.put("customResponseHeader", obj.getCustomResponseHeader().toJson());
    }
    if (obj.getDeliveryOptions() != null) {
      json.put("deliveryOptions", obj.getDeliveryOptions().toJson());
    }
    json.put("displayExceptionDetails", obj.isDisplayExceptionDetails());
    json.put("dropRequestResponseCode", obj.getDropRequestResponseCode());
    json.put("dropRequests", obj.isDropRequests());
    if (obj.getRoutingSpecificationLocation() != null) {
      json.put("routingSpecificationLocation", obj.getRoutingSpecificationLocation());
    }
    if (obj.getServerOptions() != null) {
      json.put("serverOptions", obj.getServerOptions().toJson());
    }
  }
}
