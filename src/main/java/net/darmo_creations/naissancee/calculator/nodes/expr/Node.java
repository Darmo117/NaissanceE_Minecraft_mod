package net.darmo_creations.naissancee.calculator.nodes.expr;

import net.darmo_creations.naissancee.calculator.Scope;
import net.darmo_creations.naissancee.calculator.exceptions.EvaluationException;
import net.darmo_creations.naissancee.calculator.exceptions.UndefinedVariableException;

/**
 * A node is the base component of an expression tree.
 */
public abstract class Node {
  /**
   * Evaluate this node.
   *
   * @param scope The scope to use.
   * @return The value of this node.
   * @throws EvaluationException If an error occured during evaluation.
   * @throws ArithmeticException If a math error occured.
   */
  public abstract double evaluate(final Scope scope) throws UndefinedVariableException, ArithmeticException;

  @Override
  public abstract boolean equals(Object o);

  @Override
  public abstract int hashCode();

  @Override
  public abstract String toString();
}
