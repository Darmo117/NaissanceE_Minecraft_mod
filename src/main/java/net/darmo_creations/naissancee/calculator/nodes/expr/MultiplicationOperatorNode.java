package net.darmo_creations.naissancee.calculator.nodes.expr;

import java.util.List;

/**
 * A {@link Node} representing the multiplication operator (a * b).
 */
public class MultiplicationOperatorNode extends BiOperatorNode {
  /**
   * Create a multiplication operator: left * right.
   *
   * @param left  The left operand.
   * @param right The right operand.
   */
  public MultiplicationOperatorNode(final Node left, final Node right) {
    super("*", left, right);
  }

  @Override
  protected double evaluateImpl(final List<Double> values) {
    return values.get(0) * values.get(1);
  }
}
