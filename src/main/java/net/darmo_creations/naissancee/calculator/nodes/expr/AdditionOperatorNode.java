package net.darmo_creations.naissancee.calculator.nodes.expr;

import java.util.List;

/**
 * A {@link Node} representing the addition operator (a + b).
 */
public class AdditionOperatorNode extends BiOperatorNode {
  /**
   * Create a addition operator: left + right.
   *
   * @param left  The left operand.
   * @param right The right operand.
   */
  public AdditionOperatorNode(final Node left, final Node right) {
    super("+", left, right);
  }

  @Override
  protected double evaluateImpl(final List<Double> values) {
    return values.get(0) + values.get(1);
  }
}
