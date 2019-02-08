package io.knotx.server.api.context;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Converter for {@link io.knotx.server.api.context.ClientResponse}.
 * NOTE: This class has been automatically generated from the {@link io.knotx.server.api.context.ClientResponse} original class using Vert.x codegen.
 */
public class ClientResponseConverter {

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, ClientResponse obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "body":
          if (member.getValue() instanceof String) {
            obj.setBody(io.vertx.core.buffer.Buffer.buffer(java.util.Base64.getDecoder().decode((String)member.getValue())));
          }
          break;
        case "jsonHeaders":
          if (member.getValue() instanceof JsonObject) {
            obj.setJsonHeaders(((JsonObject)member.getValue()).copy());
          }
          break;
        case "statusCode":
          if (member.getValue() instanceof Number) {
            obj.setStatusCode(((Number)member.getValue()).intValue());
          }
          break;
      }
    }
  }

  public static void toJson(ClientResponse obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(ClientResponse obj, java.util.Map<String, Object> json) {
    if (obj.getBody() != null) {
      json.put("body", java.util.Base64.getEncoder().encodeToString(obj.getBody().getBytes()));
    }
    if (obj.getJsonHeaders() != null) {
      json.put("jsonHeaders", obj.getJsonHeaders());
    }
    json.put("statusCode", obj.getStatusCode());
  }
}
