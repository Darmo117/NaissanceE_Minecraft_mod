package net.darmo_creations.naissancee.calculator.nodes;

import net.darmo_creations.naissancee.calculator.Scope;
import net.darmo_creations.naissancee.calculator.exceptions.EvaluationException;
import net.darmo_creations.naissancee.calculator.nodes.expr.Node;

import java.util.Objects;

/**
 * A statement that only contains an expression.
 */
public class ExpressionStatement extends Statement {
  private final Node node;

  /**
   * Create a statement containing an expression.
   *
   * @param node The expression {@link Node} tree.
   */
  public ExpressionStatement(final Node node) {
    this.node = Objects.requireNonNull(node);
  }

  /**
   * Evaluates the expression then returns its value.
   *
   * @return The value of the expression.
   * @throws EvaluationException If an error occured during {@link Node} evaluation.
   * @throws ArithmeticException If a math error occured.
   */
  @Override
  public String execute(final Scope scope) throws EvaluationException, ArithmeticException {
    return "" + this.node.evaluate(scope);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    return this.node.equals(((ExpressionStatement) o).node);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.node);
  }

  @Override
  public String toString() {
    return this.node.toString();
  }
}
