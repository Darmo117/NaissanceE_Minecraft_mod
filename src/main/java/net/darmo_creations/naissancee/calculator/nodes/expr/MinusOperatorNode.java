package net.darmo_creations.naissancee.calculator.nodes.expr;

/**
 * A {@link Node} representing the unary minus operator (-a).
 */
public class MinusOperatorNode extends UnaryOperatorNode {
  /**
   * Create a unary minus operator: -operand.
   *
   * @param operand The expression to compute the opposite of.
   */
  public MinusOperatorNode(final Node operand) {
    super("-", operand);
  }

  @Override
  protected double evaluateImpl(final double value) {
    return -value;
  }
}
