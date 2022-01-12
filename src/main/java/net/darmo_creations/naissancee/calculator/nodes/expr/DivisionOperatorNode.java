package net.darmo_creations.naissancee.calculator.nodes.expr;

import java.util.List;

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
  protected double evaluateImpl(final List<Double> values) throws ArithmeticException {
    double rightValue = values.get(1);
    if (rightValue == 0) {
      throw new ArithmeticException("division by 0");
    }
    return values.get(0) / rightValue;
  }
}
