package net.darmo_creations.naissancee.calculator.nodes.expr;

import java.util.Collections;
import java.util.List;

/**
 * A {@link Node} representing the unary minus operator.
 */
public class MinusOperatorNode extends OperatorNode {
  /**
   * Create a unary minus operator.
   *
   * @param operand The expression to negate.
   */
  public MinusOperatorNode(final Node operand) {
    super("-", 1, Collections.singletonList(operand));
  }

  @Override
  protected double evaluateImpl(final List<Double> values) {
    return -values.get(0);
  }

  @Override
  public String toString() {
    Node operand = this.operands.get(0);
    if (operand instanceof OperatorNode) {
      return String.format("-(%s)", operand);
    } else {
      return "-" + this.operands.get(0);
    }
  }
}
