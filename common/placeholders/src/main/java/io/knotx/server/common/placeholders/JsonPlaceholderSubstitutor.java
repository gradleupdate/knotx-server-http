package io.knotx.server.common.placeholders;

import static io.knotx.commons.json.JsonObjectUtil.getString;

import org.apache.commons.lang3.StringUtils;

import io.vertx.core.json.JsonObject;

public class JsonPlaceholderSubstitutor implements PlaceholderSubstitutor<JsonObject> {

  static final String PREFIX_PAYLOAD = "payload.";

  @Override
  public String getValue(JsonObject source, String placeholder) {
    return getString(StringUtils.substringAfter(placeholder, "."), source);
  }
}
