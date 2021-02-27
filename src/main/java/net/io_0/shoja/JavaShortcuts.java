package net.io_0.shoja;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.*;

public class JavaShortcuts {
  private JavaShortcuts() { }

  /**
   * Same as {@link Optional#ofNullable} but shorter to write
   */
  public static <T> Optional<T> opt(T value) {
    return Optional.ofNullable(value);
  }

  /**
   * Casting with {@link Optional} result
   */
  public static <T> Optional<T> optCast(Class<T> type, Object obj) {
    return opt(type).flatMap(t -> opt(obj).filter(t::isInstance).map(t::cast));
  }

  /**
   * Regex Pattern matching with {@link Optional} result
   */
  public static Optional<String> optMatch(Pattern pattern, String str) {
    return opt(pattern).flatMap(p -> opt(str).filter(s -> !s.isEmpty()).map(p::matcher).filter(Matcher::matches).map(m -> str));
  }

  /**
   * Regex Pattern matching with {@link Optional} matched groups result
   */
  public static Optional<List<String>> optMatchGroups(Pattern pattern, String str) {
    return opt(pattern).flatMap(p -> opt(str).filter(s -> !s.isEmpty()).map(p::matcher).filter(Matcher::matches).map(m ->
      range(1, m.groupCount()+1).mapToObj(m::group).collect(toList())
    )).filter(g -> !g.isEmpty());
  }
}
