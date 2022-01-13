package net.darmo_creations.naissancee.calculator.nodes.expr;

/**
 * A {@link Node} representing the equality operator (a = b).
 * Returns 1 if a = b; otherwise 0.
 */
public class EqualToOperatorNode extends BiOperatorNode {
  /**
   * Create a equality operator: left = right.
   *
   * @param left  The left operand.
   * @param right The right operand.
   */
  public EqualToOperatorNode(final Node left, final Node right) {
    super("=", left, right);
  }

  @Override
  protected double evaluateImpl(final double left, final double right) {
    return left == right ? 1 : 0;
  }
}
