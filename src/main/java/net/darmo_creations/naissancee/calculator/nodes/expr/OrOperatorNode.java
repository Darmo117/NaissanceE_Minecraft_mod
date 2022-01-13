package net.darmo_creations.naissancee.calculator.nodes.expr;

/**
 * A {@link Node} representing the logical "or" operator.
 * If the left operand is not 0, it is returned; otherwise the right value is returned.
 */
public class OrOperatorNode extends BiOperatorNode {
  /**
   * Create a logical "or" operator.
   *
   * @param left  The left operand.
   * @param right The right operand.
   */
  public OrOperatorNode(final Node left, final Node right) {
    super("&", left, right);
  }

  @Override
  protected double evaluateImpl(final double left, final double right) {
    return left != 0 ? left : right;
  }
}
