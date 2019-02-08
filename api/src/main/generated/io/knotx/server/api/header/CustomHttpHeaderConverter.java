package io.knotx.server.api.header;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Converter for {@link io.knotx.server.api.header.CustomHttpHeader}.
 * NOTE: This class has been automatically generated from the {@link io.knotx.server.api.header.CustomHttpHeader} original class using Vert.x codegen.
 */
public class CustomHttpHeaderConverter {

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, CustomHttpHeader obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "name":
          if (member.getValue() instanceof String) {
            obj.setName((String)member.getValue());
          }
          break;
        case "value":
          if (member.getValue() instanceof String) {
            obj.setValue((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(CustomHttpHeader obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(CustomHttpHeader obj, java.util.Map<String, Object> json) {
    if (obj.getName() != null) {
      json.put("name", obj.getName());
    }
    if (obj.getValue() != null) {
      json.put("value", obj.getValue());
    }
  }
}
