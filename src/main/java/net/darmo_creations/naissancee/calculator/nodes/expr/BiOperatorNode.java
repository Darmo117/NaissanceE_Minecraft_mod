package net.darmo_creations.naissancee.calculator.nodes.expr;

import net.darmo_creations.naissancee.calculator.exceptions.SyntaxErrorException;

import java.util.Arrays;

/**
 * A {@link Node} representing an operator with two operands.
 */
public abstract class BiOperatorNode extends OperatorNode {
  /**
   * Create an operator with two operands.
   *
   * @param symbol Operator’s symbol.
   * @param left   Left operand.
   * @param right  Right operand.
   * @throws SyntaxErrorException If the number of operands does not match the arity.
   */
  public BiOperatorNode(final String symbol, final Node left, final Node right) throws SyntaxErrorException {
    super(symbol, 2, Arrays.asList(left, right));
  }

  @Override
  public String toString() {
    return String.format("%s %s %s", this.operands.get(0), this.getName(), this.operands.get(1));
  }
}
