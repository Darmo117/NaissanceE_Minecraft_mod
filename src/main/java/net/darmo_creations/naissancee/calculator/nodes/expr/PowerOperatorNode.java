package net.darmo_creations.naissancee.calculator.nodes.expr;

/**
 * A {@link Node} representing the exponent operator (a<sup>b</sup>).
 */
public class PowerOperatorNode extends BiOperatorNode {
  /**
   * Create an exponent operator: left<sup>right</sup>.
   *
   * @param left  The left operand.
   * @param right The right operand.
   */
  public PowerOperatorNode(final Node left, final Node right) {
    super("^", left, right);
  }

  @Override
  protected double evaluateImpl(final double left, final double right) {
    return Math.pow(left, right);
  }
}
