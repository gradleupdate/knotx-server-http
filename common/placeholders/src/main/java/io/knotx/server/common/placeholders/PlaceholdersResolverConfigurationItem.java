package io.knotx.server.common.placeholders;

import java.util.List;
import java.util.stream.Collectors;

class PlaceholdersResolverConfigurationItem {

  private final List<PlaceholderSubstitutor> placeholderSubstitutor;
  private final List<String> prefixes;

  PlaceholdersResolverConfigurationItem(
      List<PlaceholderSubstitutor> placeholderSubstitutor, List<String> prefixes) {
    this.placeholderSubstitutor = placeholderSubstitutor;
    this.prefixes = prefixes;
  }

  List<String> getPrefixes() {
    return prefixes;
  }

  List<PlaceholderSubstitutor> getPlaceholderSubstitutors() {
    return placeholderSubstitutor;
  }

  List<String> getPlaceholdersForSource(List<String> allPlaceholders) {
    return allPlaceholders.stream()
        .filter(this::startsWith)
        .collect(Collectors.toList());
  }

  private boolean startsWith(String placeholder) {
    return prefixes.stream()
        .anyMatch(placeholder::startsWith);
  }
}
