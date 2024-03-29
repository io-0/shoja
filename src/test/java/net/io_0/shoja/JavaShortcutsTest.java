package net.io_0.shoja;

import net.io_0.shoja.model.Item;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.lang.System.out;
import static java.time.OffsetDateTime.now;
import static java.util.Optional.empty;
import static java.util.regex.Pattern.compile;
import static net.io_0.shoja.JavaShortcuts.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Narrative:
 *   As a programmer
 *
 *   I want to collect useful utilities
 *   so that I can reuse them anywhere
 */
class JavaShortcutsTest {
  /**
   * Scenario: I can't be bothered to write Optional.ofNullable every time.
   */
  @Test
  @SuppressWarnings("all")
  void testOpt() {
    assertEquals(Optional.ofNullable(null), opt(null));
    assertEquals(Optional.ofNullable("not null"), opt("not null"));
  }

  /**
   * Scenario: I want to Cast an Object with an optional result
   */
  @Test
  void testCast() {
    assertEquals(empty(), cast(null, null));
    assertEquals(empty(), cast(null, String.class));
    assertEquals(empty(), cast(1, String.class));
    assertEquals(opt("one"), cast("one", String.class));
  }

  /**
   * Scenario: I want to Regex match a String with an optional result
   */
  @Test
  void testMatch() {
    assertEquals(empty(), match(null, null));
    assertEquals(empty(), match(null, compile(".*")));
    assertEquals(empty(), match("", compile("")));
    assertEquals(empty(), match("", compile(".*")));
    assertEquals(opt("t"), match("t", compile(".*")).map(RegexMatches::first));

    // with groups
    assertEquals(empty(), match("", compile("(.*)")));
    assertEquals(empty(), match("t", compile("(e)")));
    assertEquals(empty(), match("2021-02-2", compile("([0-9]{4})-([0-9]{2})-([0-9]{2})")));
    assertEquals(opt("t"), match("t", compile("(t)")).map(RegexMatches::first));
    assertEquals(opt(List.of("t")), match("t", compile("(.*)")).map(RegexMatches::all));
    assertEquals(opt(List.of("2021", "02", "27")), match("2021-02-27", compile("([0-9]{4})-([0-9]{2})-([0-9]{2})")).map(RegexMatches::all));
    assertEquals(opt(List.of("tes", "es")), match("test", compile("(t(es)).*")).map(RegexMatches::all));
  }

  /**
   * Scenario: I want fluent code and a band-aid if no fluent setters or builders are present
   */
  @Test
  void testTap() {
    assertNull(tap(null, null));
    assertNull(tap(null, obj -> {}));
    assertEquals("one", tap(new ConcurrentSkipListSet<String>(), set -> { set.add("one"); set.add("two"); }).first());
    assertNotNull(tap(new Item("b"), b -> // tap spares me from variable assignment
      Stream.of(new Item("a"), b, new Item("c"))
        .map(tap(item -> item.setProcessedAt(now()))) // tap allows fluent object manipulation in this exemplary item stream workflow
        .forEach(item -> out.printf("Item %s was processed at %s%n", item.getName(), item.getProcessedAt()))
    ).getProcessedAt());
  }

  /**
   * Scenario: Sometimes I want to be spared from creating variables and stay in the flow
   */
  @Test
  void testWith() {
    assertNull(with(null, null));
    assertNull(with("a", null));
    assertTrue(with(LocalDateTime.now(), now -> format("%s is after %s", now, now.minusHours(1))).contains("after 2"));
  }

  /**
   * Scenario: I want to be able to compose method references
   */
  @Test
  void testFn() {
    assertEquals(4, fn(this::subtractTwo).compose(this::addFive).apply(1));
    assertEquals(6, fn(this::addFive).andThen(this::subtractTwo).apply(3));
  }
  private Integer addFive(Integer nr) { return nr+5; }
  private Integer subtractTwo(Integer nr) { return nr-2; }

  /**
   * Scenario: I want a compact way to do something if a value is not null, a List not empty or a Map not empty
   */
  @Test
  void testIfPresent() {
    StringBuilder sb = new StringBuilder();
    ifPresent(null, null); // no NPE
    ifPresent("t", null); // no NPE
    ifPresent(null, o -> { throw new IllegalStateException(); }); // no Ex
    ifPresent(List.of(), o -> { throw new IllegalStateException(); }); // no Ex
    ifPresent(Map.of(), o -> { throw new IllegalStateException(); }); // no Ex
    ifPresent("t", sb::append);
    ifPresent(List.of("e"), sb::append);
    ifPresent(Map.of("s", "t"), sb::append);
    assertEquals("t[e]{s=t}", sb.toString());
  }

  /**
   * Scenario: I want a compact way to do something if a value is null, a List empty or a Map empty
   */
  @Test
  void testIfAbsent() {
    StringBuilder sb = new StringBuilder();
    ifAbsent(null, null); // no NPE
    ifAbsent("t", null); // no NPE
    ifAbsent(null, () -> sb.append("t"));
    ifAbsent("t", () -> { throw new IllegalStateException(); }); // no Ex
    ifAbsent(List.of(), () -> sb.append("e"));
    ifAbsent(List.of("e"), () -> { throw new IllegalStateException(); }); // no Ex
    ifAbsent(Map.of(), () -> sb.append("st"));
    ifAbsent(Map.of("s", "t"), () -> { throw new IllegalStateException(); }); // no Ex
    assertEquals("test", sb.toString());
  }
}