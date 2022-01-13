package net.darmo_creations.naissancee.calculator.nodes.expr;

import java.util.Collections;
import java.util.List;

/**
 * A {@link Node} representing an operator with one operand.
 */
public abstract class UnaryOperatorNode extends OperatorNode {
  /**
   * Create a unary operator.
   *
   * @param symbol  Operator’s symbol.
   * @param operand Operator’s operand.
   */
  public UnaryOperatorNode(final String symbol, final Node operand) {
    super(symbol, 1, Collections.singletonList(operand));
  }

  @Override
  protected final double evaluateImpl(final List<Double> values) {
    return this.evaluateImpl(values.get(0));
  }

  /**
   * Delegate method that returns the result of the operator.
   *
   * @param value Value of the operand.
   * @return Operator’s result.
   */
  protected abstract double evaluateImpl(final double value);

  @Override
  public String toString() {
    Node operand = this.operands.get(0);
    if (operand instanceof OperatorNode) {
      return String.format("%s(%s)", this.getName(), operand);
    } else {
      return this.getName() + this.operands.get(0);
    }
  }
}
