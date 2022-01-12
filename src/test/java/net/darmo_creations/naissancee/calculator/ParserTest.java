package net.darmo_creations.naissancee.calculator;

import net.darmo_creations.naissancee.calculator.nodes.ExpressionStatement;
import net.darmo_creations.naissancee.calculator.nodes.expr.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParserTest {
  @Test
  void testParsePositiveInt() {
    assertEquals(new ExpressionStatement(new NumberNode(1)), Parser.parse("1"));
  }

  @Test
  void testParsePositiveFloat() {
    assertEquals(new ExpressionStatement(new NumberNode(1)), Parser.parse("1.0"));
    assertEquals(new ExpressionStatement(new NumberNode(1)), Parser.parse("1."));
  }

  @Test
  void testParsePlusOpNumber() {
    assertEquals(new ExpressionStatement(new NumberNode(1)), Parser.parse("+1"));
    assertEquals(new ExpressionStatement(new NumberNode(1)), Parser.parse("+1.0"));
    assertEquals(new ExpressionStatement(new NumberNode(1)), Parser.parse("+1."));
  }

  @Test
  void testParseMinusOpNumber() {
    assertEquals(new ExpressionStatement(new MinusOperatorNode(new NumberNode(1))), Parser.parse("-1"));
    assertEquals(new ExpressionStatement(new MinusOperatorNode(new NumberNode(1))), Parser.parse("-1.0"));
    assertEquals(new ExpressionStatement(new MinusOperatorNode(new NumberNode(1))), Parser.parse("-1."));
  }

  @Test
  void testParseParenthesesNumber() {
    assertEquals(new ExpressionStatement(new NumberNode(1)), Parser.parse("(1)"));
    assertEquals(new ExpressionStatement(new NumberNode(1)), Parser.parse("(1.0)"));
    assertEquals(new ExpressionStatement(new NumberNode(1)), Parser.parse("(1.)"));
  }

  @Test
  void testParseVariable() {
    assertEquals(new ExpressionStatement(new VariableNode("var")), Parser.parse("var"));
  }

  @Test
  void testParseAddOpNumbers() {
    assertEquals(new ExpressionStatement(new AdditionOperatorNode(new NumberNode(1), new NumberNode(2))), Parser.parse("1 + 2"));
  }

  @Test
  void testParseSubOpNumbers() {
    assertEquals(new ExpressionStatement(new SubtractionOperatorNode(new NumberNode(1), new NumberNode(2))), Parser.parse("1 - 2"));
  }

  @Test
  void testParseMulOpNumbers() {
    assertEquals(new ExpressionStatement(new MultiplicationOperatorNode(new NumberNode(1), new NumberNode(2))), Parser.parse("1 * 2"));
  }

  @Test
  void testParseDivOpNumbers() {
    assertEquals(new ExpressionStatement(new DivisionOperatorNode(new NumberNode(1), new NumberNode(2))), Parser.parse("1 / 2"));
  }

  @Test
  void testParseModOpNumbers() {
    assertEquals(new ExpressionStatement(new ModuloOperatorNode(new NumberNode(1), new NumberNode(2))), Parser.parse("1 % 2"));
  }

  @Test
  void testParsePowOpNumbers() {
    assertEquals(new ExpressionStatement(new PowerOperatorNode(new NumberNode(1), new NumberNode(2))), Parser.parse("1 ^ 2"));
  }
}
