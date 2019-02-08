package io.knotx.server.api.context;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Converter for {@link io.knotx.server.api.context.ClientRequest}.
 * NOTE: This class has been automatically generated from the {@link io.knotx.server.api.context.ClientRequest} original class using Vert.x codegen.
 */
public class ClientRequestConverter {

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, ClientRequest obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "jsonFormAttributes":
          if (member.getValue() instanceof JsonObject) {
            obj.setJsonFormAttributes(((JsonObject)member.getValue()).copy());
          }
          break;
        case "jsonHeaders":
          if (member.getValue() instanceof JsonObject) {
            obj.setJsonHeaders(((JsonObject)member.getValue()).copy());
          }
          break;
        case "jsonParams":
          if (member.getValue() instanceof JsonObject) {
            obj.setJsonParams(((JsonObject)member.getValue()).copy());
          }
          break;
        case "method":
          if (member.getValue() instanceof String) {
            obj.setMethod(io.vertx.core.http.HttpMethod.valueOf((String)member.getValue()));
          }
          break;
        case "path":
          if (member.getValue() instanceof String) {
            obj.setPath((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(ClientRequest obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(ClientRequest obj, java.util.Map<String, Object> json) {
    if (obj.getJsonFormAttributes() != null) {
      json.put("jsonFormAttributes", obj.getJsonFormAttributes());
    }
    if (obj.getJsonHeaders() != null) {
      json.put("jsonHeaders", obj.getJsonHeaders());
    }
    if (obj.getJsonParams() != null) {
      json.put("jsonParams", obj.getJsonParams());
    }
    if (obj.getMethod() != null) {
      json.put("method", obj.getMethod().name());
    }
    if (obj.getPath() != null) {
      json.put("path", obj.getPath());
    }
  }
}
