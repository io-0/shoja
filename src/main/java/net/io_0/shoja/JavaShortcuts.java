package net.io_0.shoja;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
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
  public static <T> Optional<T> cast(Object obj, Class<T> type) {
    return opt(type).flatMap(t -> opt(obj).filter(t::isInstance).map(t::cast));
  }

  /**
   * Simplified Regex {@link Pattern} matching with {@link Optional} matches (supports unnamed regex groups)
   */
  public static Optional<RegexMatches> match(String str, Pattern pattern) {
    return opt(pattern)
      .flatMap(p -> opt(str).filter(not(String::isEmpty)).map(p::matcher).filter(Matcher::matches))
      .map(result -> result.groupCount() > 0
        ? range(1, result.groupCount()+1).mapToObj(result::group).collect(toList())
        : List.of(str)
      )
      .filter(not(List::isEmpty))
      .map(RegexMatches::new);
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
   * Execute function with variable (reversed apply).
   */
  public static <T, U> U with(T var, Function<T, U> fn) {
    return opt(fn).map(f -> f.apply(var)).orElse(null);
  }

  /**
   * Invoke callback if data or data within container (collection or map) is present
   */
  public static <T> void ifPresent(T obj, Consumer<T> callback) {
    opt(callback).ifPresent(cb -> {
      if (isPresent.test(obj)) {
        cb.accept(obj);
      }
    });
  }

  /**
   * Invoke callback if data or data within container (collection or map) is absent
   */
  public static <T> void ifAbsent(T obj, Runnable callback) {
    opt(callback).ifPresent(cb -> {
      if (isAbsent.test(obj)) {
        cb.run();
      }
    });
  }

  public static final Predicate<Object> isEmptyCollection = o -> o instanceof Collection && ((Collection<?>) o).isEmpty();
  public static final Predicate<Object> isEmptyMap = o -> o instanceof Map && ((Map<?, ?>) o).isEmpty();
  public static final Predicate<Object> isAbsent = isEmptyCollection.or(isEmptyMap).or(Objects::isNull);
  public static final Predicate<Object> isPresent = not(isAbsent);
}
