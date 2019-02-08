package io.knotx.server.handler.http.request.body;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Converter for {@link io.knotx.server.handler.http.request.body.BodyHandlerOptions}.
 * NOTE: This class has been automatically generated from the {@link io.knotx.server.handler.http.request.body.BodyHandlerOptions} original class using Vert.x codegen.
 */
 class BodyHandlerOptionsConverter {

   static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, BodyHandlerOptions obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "fileUploadDirectory":
          if (member.getValue() instanceof String) {
            obj.setFileUploadDirectory((String)member.getValue());
          }
          break;
        case "fileUploadLimit":
          if (member.getValue() instanceof Number) {
            obj.setFileUploadLimit(((Number)member.getValue()).longValue());
          }
          break;
      }
    }
  }

   static void toJson(BodyHandlerOptions obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

   static void toJson(BodyHandlerOptions obj, java.util.Map<String, Object> json) {
    if (obj.getFileUploadDirectory() != null) {
      json.put("fileUploadDirectory", obj.getFileUploadDirectory());
    }
    if (obj.getFileUploadLimit() != null) {
      json.put("fileUploadLimit", obj.getFileUploadLimit());
    }
  }
}
