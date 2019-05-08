/*
 * Copyright (C) 2019 Knot.x Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.knotx.server.common.placeholders;

import io.knotx.server.api.context.ClientRequest;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

public final class PlaceholdersResolver {

  private static final Logger LOGGER = LoggerFactory.getLogger(PlaceholdersResolver.class);

  private static List<PlaceholderSubstitutor> placeholderSubstitutors =
      Arrays.asList(new RequestPlaceholderSubstitutor(), new UriPlaceholderSubstitutor());

  private PlaceholdersResolver() {
    // util
  }

  public static String resolve(String stringWithPlaceholders, ClientRequest request) {
    String resolved = stringWithPlaceholders;
    List<String> placeholders = getPlaceholders(stringWithPlaceholders);

    for (String placeholder : placeholders) {
      resolved = resolved.replace("{" + placeholder + "}",
          encodeValue(getPlaceholderValue(request, placeholder)));
    }

    return resolved;
  }

  protected static List<String> getPlaceholders(String serviceUri) {
    return Arrays.stream(serviceUri.split("\\{"))
        .filter(str -> str.contains("}"))
        .map(str -> StringUtils.substringBefore(str, "}"))
        .collect(Collectors.toList());
  }

  private static String getPlaceholderValue(ClientRequest request, String placeholder) {
    return placeholderSubstitutors.stream()
        .map(substitutor -> substitutor.getValue(request, placeholder))
        .filter(Objects::nonNull)
        .findFirst()
        .orElse("");
  }

  private static String encodeValue(String value) {
    try {
      return URLEncoder.encode(value, "UTF-8").replace("+", "%20").replace("%2F", "/");
    } catch (UnsupportedEncodingException ex) {
      LOGGER.fatal("Unexpected Exception - Unsupported encoding UTF-8", ex);
      throw new UnsupportedCharsetException("UTF-8");
    }
  }
}
