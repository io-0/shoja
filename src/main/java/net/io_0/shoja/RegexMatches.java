package net.io_0.shoja;

import java.util.List;

import static java.util.Collections.unmodifiableList;
import static java.util.Objects.isNull;

public class RegexMatches {
  private final List<String> matches;

  public RegexMatches(List<String> source) {
    if (isNull(source) || source.isEmpty()) {
      throw new IllegalArgumentException("List can't be null or empty");
    }
    this.matches = unmodifiableList(source);
  }

  public String first() {
    return matches.get(0);
  }

  public List<String> all() {
    return matches;
  }
}
