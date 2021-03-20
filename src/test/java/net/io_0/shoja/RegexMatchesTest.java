package net.io_0.shoja;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RegexMatchesTest {
  @Test
  void testGuard() {
    assertThrows(IllegalArgumentException.class, () -> new RegexMatches(null));
    assertThrows(IllegalArgumentException.class, () -> new RegexMatches(List.of()));
  }
}