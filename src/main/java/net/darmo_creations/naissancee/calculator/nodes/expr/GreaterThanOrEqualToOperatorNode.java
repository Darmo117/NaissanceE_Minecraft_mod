package net.darmo_creations.naissancee.calculator.nodes.expr;

/**
 * A {@link Node} representing the "greater than or equal to" operator (a ≥ b).
 * Returns 1 if a ≥ b; otherwise 0.
 */
public class GreaterThanOrEqualToOperatorNode extends BiOperatorNode {
  /**
   * Create a "greater than or equal to" operator: left ≥ right.
   *
   * @param left  The left operand.
   * @param right The right operand.
   */
  public GreaterThanOrEqualToOperatorNode(final Node left, final Node right) {
    super(">=", left, right);
  }

  @Override
  protected double evaluateImpl(final double left, final double right) {
    return left >= right ? 1 : 0;
  }
}