package net.darmo_creations.naissancee.calculator.nodes.expr;

/**
 * A {@link Node} representing the "less than or equal to" operator (a ≤ b).
 * Returns 1 if a ≤ b; otherwise 0.
 */
public class LessThanOrEqualToOperatorNode extends BiOperatorNode {
  /**
   * Create a "less than or equal to" operator: left ≤ right.
   *
   * @param left  The left operand.
   * @param right The right operand.
   */
  public LessThanOrEqualToOperatorNode(final Node left, final Node right) {
    super("<=", left, right);
  }

  @Override
  protected double evaluateImpl(final double left, final double right) {
    return left <= right ? 1 : 0;
  }
}
