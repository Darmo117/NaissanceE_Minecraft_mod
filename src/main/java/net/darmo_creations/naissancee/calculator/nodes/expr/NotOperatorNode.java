package net.darmo_creations.naissancee.calculator.nodes.expr;

/**
 * A {@link Node} representing the not operator (not a). Returns 1 if the number is 0, and 0 if the number is not 0.
 */
public class NotOperatorNode extends UnaryOperatorNode {
  /**
   * Create a not operator: not operand.
   *
   * @param operand The expression to compute the negation of.
   */
  public NotOperatorNode(final Node operand) {
    super("!", operand);
  }

  @Override
  protected double evaluateImpl(final double value) {
    return value == 0 ? 1 : 0;
  }
}
