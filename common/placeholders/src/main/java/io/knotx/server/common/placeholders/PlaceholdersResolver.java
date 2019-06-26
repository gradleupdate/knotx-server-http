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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import io.knotx.server.common.placeholders.configuration.PlaceholdersResolverConfiguration;
import io.knotx.server.common.placeholders.configuration.PlaceholdersResolverConfigurationItem;
import io.knotx.server.common.placeholders.configuration.SourceDefinition;
import io.knotx.server.common.placeholders.configuration.SourceDefinitions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public final class PlaceholdersResolver {

  private static final Logger LOGGER = LoggerFactory.getLogger(PlaceholdersResolver.class);

  private PlaceholdersResolver() {
    // util
  }

  public static String resolve(String stringWithPlaceholders, SourceDefinitions sources) {
    PlaceholdersResolverConfiguration configuration = PlaceholdersResolverConfiguration.fromSourceDefinitions(
        sources);

    String resolved = stringWithPlaceholders;
    List<String> allPlaceholders = getPlaceholders(stringWithPlaceholders);

    for (SourceDefinition sourceDefinition : sources.getSourceDefinitions()) {
      PlaceholdersResolverConfigurationItem configurationItem = configuration.getItem(
          sourceDefinition.getSourceClass());

      if (configurationItem == null) {
        continue;
      }

      resolved = resolve(resolved, allPlaceholders, sourceDefinition.getSource(),
          configurationItem);

    }

    resolved = clearUnresolved(resolved);

    return resolved;
  }

  private static String resolve(String resolved, List<String> allPlaceholders, Object source,
      PlaceholdersResolverConfigurationItem configurationItem) {
    List<String> placeholders = configurationItem.getPlaceholdersForSource(allPlaceholders);
    for (String placeholder : placeholders) {
      resolved = replace(resolved, placeholder,
          getPlaceholderValue(source, configurationItem, placeholder));
    }
    return resolved;
  }

  private static String clearUnresolved(String resolved) {
    List<String> unresolved = getPlaceholders(resolved);

    for (String unreslvedPlaceholder : unresolved) {
      resolved = replace(resolved, unreslvedPlaceholder, "");
    }
    return resolved;
  }

  private static String replace(String resolved, String placeholder, String value) {
    return resolved.replace("{" + placeholder + "}",
        encodeValue(value));
  }


  protected static List<String> getPlaceholders(String serviceUri) {
    return Arrays.stream(serviceUri.split("\\{"))
        .filter(str -> str.contains("}"))
        .map(str -> StringUtils.substringBefore(str, "}"))
        .collect(Collectors.toList());
  }

  private static <T> String getPlaceholderValue(T source,
      PlaceholdersResolverConfigurationItem configurationItem, String placeholder) {
    return configurationItem.getPlaceholderSubstitutors()
        .stream()
        .map(substitutor -> substitutor.getValue(source, placeholder))
        .filter(Objects::nonNull)
        .findFirst()
        .orElse("");
  }

  private static String encodeValue(String value) {
    try {
      return URLEncoder.encode(value, "UTF-8")
          .replace("+", "%20")
          .replace("%2F", "/");
    } catch (UnsupportedEncodingException ex) {
      LOGGER.fatal("Unexpected Exception - Unsupported encoding UTF-8", ex);
      throw new UnsupportedCharsetException("UTF-8");
    }
  }
}
