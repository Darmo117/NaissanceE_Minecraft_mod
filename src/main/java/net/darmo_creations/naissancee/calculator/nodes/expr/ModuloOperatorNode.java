package net.darmo_creations.naissancee.calculator.nodes.expr;

import net.darmo_creations.naissancee.Utils;

/**
 * A {@link Node} representing the modulo operator, i.e the remainder of the division of a / b.
 */
public class ModuloOperatorNode extends BiOperatorNode {
  /**
   * Create a modulo operator: remainder of a / b.
   *
   * @param left  The left operand.
   * @param right The right operand.
   */
  public ModuloOperatorNode(final Node left, final Node right) {
    super("%", left, right);
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
    return Utils.trueModulo(left, right);
  }
}
