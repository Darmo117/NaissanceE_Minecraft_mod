package net.darmo_creations.naissancee.calculator.nodes.expr;

import java.util.List;

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
  protected double evaluateImpl(final List<Double> values) {
    return Math.pow(values.get(0), values.get(1));
  }
}
