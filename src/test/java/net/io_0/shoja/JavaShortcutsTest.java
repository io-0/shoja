package net.io_0.shoja;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

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
  void testOpt() {
    assertEquals(Optional.ofNullable(null), opt(null));
    assertEquals(Optional.ofNullable("not null"), opt("not null"));
  }

  /**
   * Scenario: I want to Cast an Object with an optional result
   */
  @Test
  void testOptCast() {
    assertEquals(empty(), optCast(null, null));
    assertEquals(empty(), optCast(String.class, null));
    assertEquals(empty(), optCast(String.class, 1));
    assertEquals(opt("one"), optCast(String.class, "one"));
  }

  /**
   * Scenario: I want to Regex match a String with an optional result
   */
  @Test
  void testOptMatch() {
    assertEquals(empty(), optMatch(null, null));
    assertEquals(empty(), optMatch(compile(".*"), null));
    assertEquals(empty(), optMatch(compile(".*"), ""));
    assertEquals(opt("t"), optMatch(compile(".*"), "t"));
  }

  /**
   * Scenario: I want to Regex with groups match a String with an optional result
   */
  @Test
  void testOptMatchGroups() {
    assertEquals(empty(), optMatchGroups(null, null));
    assertEquals(empty(), optMatchGroups(compile(".*"), null));
    assertEquals(empty(), optMatchGroups(compile(".*"), ""));
    assertEquals(empty(), optMatchGroups(compile(".*"), "t"));
    assertEquals(empty(), optMatchGroups(compile("(.*)"), ""));
    assertEquals(empty(), optMatchGroups(compile("(e)"), "t"));
    assertEquals(opt(List.of("t")), optMatchGroups(compile("(.*)"), "t"));
    assertEquals(opt(List.of("2021", "02", "27")), optMatchGroups(compile("([0-9]{4})-([0-9]{2})-([0-9]{2})"), "2021-02-27"));
    assertEquals(opt(List.of("tes", "es")), optMatchGroups(compile("(t(es)).*"), "test"));
  }
}