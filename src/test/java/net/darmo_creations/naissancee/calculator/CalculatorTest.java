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
    this.c = new Calculator();
  }

  @Test
  void testEvaluate() {
    //noinspection OptionalGetWithoutIsPresent
    assertEquals(2.0, this.c.evaluate("1 + 1").getValue().get());
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
    for (int i = 0; i < Calculator.MAX_VARS_PER_PLAYER; i++) {
      final String statement = String.format("a%d := 1", i);
      assertDoesNotThrow(() -> this.c.evaluate(statement));
    }
    assertThrows(MaxDefinitionsException.class, () -> this.c.evaluate("excess := 1"));
  }

  @Test
  void testReset() {
    this.c.evaluate("a := 1");
    assertEquals(Collections.singletonMap("a", 1.0), this.c.getVariables());
    this.c.reset();
    assertEquals(Collections.emptyMap(), this.c.getVariables());
  }
}
