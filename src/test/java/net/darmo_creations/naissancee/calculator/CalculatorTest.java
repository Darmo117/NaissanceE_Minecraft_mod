package net.darmo_creations.naissancee.calculator;

import net.darmo_creations.naissancee.calculator.exceptions.MaxDefinitionsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {
  Calculator c;

  @BeforeEach
  void setUp() {
    this.c = new Calculator(2);
  }

  @Test
  void testEvaluate() {
    assertEquals("2.0", this.c.evaluate("1 + 1"));
  }

  @Test
  void testDeclareVar() {
    assertFalse(this.c.getVariables().containsKey("a"));
    this.c.evaluate("a := 1");
    assertEquals(1.0, this.c.getVariables().get("a"));
  }

  @Test
  void testDeleteVar() {
    this.c.evaluate("a := 1");
    assertEquals(1.0, this.c.getVariables().get("a"));
    this.c.deleteVariable("a");
    assertFalse(this.c.getVariables().containsKey("a"));
  }

  @Test
  void testOverrideVar() {
    this.c.evaluate("a := 1");
    assertEquals(1.0, this.c.getVariables().get("a"));
    this.c.evaluate("a := 2");
    assertEquals(2.0, this.c.getVariables().get("a"));
  }

  @Test
  void testMaxVarsReached() {
    assertDoesNotThrow(() -> this.c.evaluate("a := 1"));
    assertDoesNotThrow(() -> this.c.evaluate("b := 1"));
    assertThrows(MaxDefinitionsException.class, () -> this.c.evaluate("c := 1"));
  }

  @Test
  void testReset() {
    this.c.evaluate("a := 1");
    assertEquals(Collections.singletonMap("a", 1.0), this.c.getVariables());
    this.c.reset();
    assertEquals(Collections.emptyMap(), this.c.getVariables());
  }
}
