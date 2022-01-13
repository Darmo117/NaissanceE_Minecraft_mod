package net.darmo_creations.naissancee.calculator.nodes.expr;

/**
 * A {@link Node} representing the subtraction operator (a - b).
 */
public class SubtractionOperatorNode extends BiOperatorNode {
  /**
   * Create a subtraction operator: left - right.
   *
   * @param left  The left operand.
   * @param right The right operand.
   */
  public SubtractionOperatorNode(final Node left, final Node right) {
    super("-", left, right);
  }

  @Override
  protected double evaluateImpl(final double left, final double right) {
    return left - right;
  }
}
