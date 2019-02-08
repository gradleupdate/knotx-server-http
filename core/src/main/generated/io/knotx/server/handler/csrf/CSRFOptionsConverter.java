package io.knotx.server.handler.csrf;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Converter for {@link io.knotx.server.handler.csrf.CSRFOptions}.
 * NOTE: This class has been automatically generated from the {@link io.knotx.server.handler.csrf.CSRFOptions} original class using Vert.x codegen.
 */
 class CSRFOptionsConverter {

   static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, CSRFOptions obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "cookieName":
          if (member.getValue() instanceof String) {
            obj.setCookieName((String)member.getValue());
          }
          break;
        case "cookiePath":
          if (member.getValue() instanceof String) {
            obj.setCookiePath((String)member.getValue());
          }
          break;
        case "headerName":
          if (member.getValue() instanceof String) {
            obj.setHeaderName((String)member.getValue());
          }
          break;
        case "secret":
          if (member.getValue() instanceof String) {
            obj.setSecret((String)member.getValue());
          }
          break;
        case "timeout":
          if (member.getValue() instanceof Number) {
            obj.setTimeout(((Number)member.getValue()).longValue());
          }
          break;
      }
    }
  }

   static void toJson(CSRFOptions obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

   static void toJson(CSRFOptions obj, java.util.Map<String, Object> json) {
    if (obj.getCookieName() != null) {
      json.put("cookieName", obj.getCookieName());
    }
    if (obj.getCookiePath() != null) {
      json.put("cookiePath", obj.getCookiePath());
    }
    if (obj.getHeaderName() != null) {
      json.put("headerName", obj.getHeaderName());
    }
    if (obj.getSecret() != null) {
      json.put("secret", obj.getSecret());
    }
    json.put("timeout", obj.getTimeout());
  }
}
