package net.darmo_creations.naissancee.calculator.nodes.expr;

/**
 * A {@link Node} representing the logical "and" operator.
 * If the left operand is 0, 0 is returned; otherwise the right operand is returned.
 */
public class AndOperatorNode extends BiOperatorNode {
  /**
   * Create a logical "and" operator.
   *
   * @param left  The left operand.
   * @param right The right operand.
   */
  public AndOperatorNode(final Node left, final Node right) {
    super("&", left, right);
  }

  @Override
  protected double evaluateImpl(final double left, final double right) {
    return left == 0 ? 0 : right;
  }
}
