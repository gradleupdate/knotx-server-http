package io.knotx.server.handler.logger;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Converter for {@link io.knotx.server.handler.logger.AccessLogOptions}.
 * NOTE: This class has been automatically generated from the {@link io.knotx.server.handler.logger.AccessLogOptions} original class using Vert.x codegen.
 */
 class AccessLogOptionsConverter {

   static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, AccessLogOptions obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "enabled":
          if (member.getValue() instanceof Boolean) {
            obj.setEnabled((Boolean)member.getValue());
          }
          break;
        case "format":
          if (member.getValue() instanceof String) {
            obj.setFormat(io.vertx.ext.web.handler.LoggerFormat.valueOf((String)member.getValue()));
          }
          break;
        case "immediate":
          if (member.getValue() instanceof Boolean) {
            obj.setImmediate((Boolean)member.getValue());
          }
          break;
      }
    }
  }

   static void toJson(AccessLogOptions obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

   static void toJson(AccessLogOptions obj, java.util.Map<String, Object> json) {
    json.put("enabled", obj.isEnabled());
    if (obj.getFormat() != null) {
      json.put("format", obj.getFormat().name());
    }
    json.put("immediate", obj.isImmediate());
  }
}
