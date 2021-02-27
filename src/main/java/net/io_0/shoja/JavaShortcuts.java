package net.io_0.shoja;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.function.Predicate.not;
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
  public static <T> Optional<T> optCast(Object obj, Class<T> type) {
    return opt(type).flatMap(t -> opt(obj).filter(t::isInstance).map(t::cast));
  }

  /**
   * Regex {@link Pattern} matching with {@link Optional} result
   */
  public static Optional<String> optMatch(String str, Pattern pattern) {
    return match(str, pattern).map(result -> str);
  }

  /**
   * Regex {@link Pattern} matching with {@link Optional} matched groups result
   */
  public static Optional<List<String>> optMatchGroups(String str, Pattern pattern) {
    return match(str, pattern)
      .map(result -> range(1, result.groupCount()+1).mapToObj(result::group).collect(toList()))
      .filter(not(List::isEmpty));
  }

  /**
   * Manipulate / tap into an {@link Object}
   */
  public static <T> T tap(T obj, Consumer<T> manipulator) {
    opt(manipulator).ifPresent(m -> m.accept(obj));
    return obj;
  }

  /**
   * Curried version of {@link #tap(Object, Consumer)}
   */
  public static <T> UnaryOperator<T> tap(Consumer<T> manipulator) {
    return obj -> tap(obj, manipulator);
  }

  /**
   * Calls consumer if data or data within container (collection or map) is present
   */
  public static <T> void ifPresent(T obj, Consumer<T> consumer) {
    opt(consumer).ifPresent(c -> opt(obj)
      .filter(not(isEmptyCollection.or(isEmptyMap)))
      .ifPresent(c)
    );
  }

  /**
   * Calls runnable if data or data within container (collection or map) is absent
   */
  public static <T> void ifAbsent(T obj, Runnable runnable) {
    opt(runnable).ifPresent(r -> {
      if (isEmptyCollection.or(isEmptyMap).or(Objects::isNull).test(obj)) {
        r.run();
      }
    });
  }

  private static Optional<MatchResult> match(String str, Pattern pattern) {
    return opt(pattern).flatMap(p -> opt(str).filter(not(String::isEmpty)).map(p::matcher).filter(Matcher::matches));
  }

  private static final Predicate<Object> isEmptyCollection = o -> o instanceof Collection && ((Collection<?>) o).isEmpty();

  private static final Predicate<Object> isEmptyMap = o -> o instanceof Map && ((Map<?, ?>) o).isEmpty();
}
