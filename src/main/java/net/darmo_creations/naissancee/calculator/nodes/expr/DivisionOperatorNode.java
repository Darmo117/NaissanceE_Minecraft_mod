package net.darmo_creations.naissancee.calculator.nodes.expr;

/**
 * A {@link Node} representing the division operator (a / b).
 */
public class DivisionOperatorNode extends BiOperatorNode {
  /**
   * Create a division operator: left / right.
   *
   * @param left  The left operand.
   * @param right The right operand.
   */
  public DivisionOperatorNode(final Node left, final Node right) {
    super("/", left, right);
  }

  /**
   * {@inheritDoc}
   *
   * @throws ArithmeticException If the right operand is 0.
   */
  @Override
  protected double evaluateImpl(final double left, final double right) throws ArithmeticException {
    if (right == 0) {
      throw new ArithmeticException("division by 0");
    }
    return left / right;
  }
}
