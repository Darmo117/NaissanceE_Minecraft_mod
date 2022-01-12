package net.darmo_creations.naissancee.calculator.nodes.expr;

import net.darmo_creations.naissancee.calculator.Scope;
import net.darmo_creations.naissancee.calculator.exceptions.EvaluationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A {@link Node} representing the call to a function.
 */
public class FunctionNode extends Node {
  private final String name;
  protected final List<Node> operands;

  /**
   * Create a function call.
   *
   * @param name     Function’s name.
   * @param operands Function’s arguments.
   */
  public FunctionNode(final String name, final List<Node> operands) {
    this.name = Objects.requireNonNull(name);
    this.operands = new ArrayList<>(operands);
  }

  /**
   * Return function’s name.
   */
  public String getName() {
    return this.name;
  }

  /**
   * Evaluate the function then return its value.
   *
   * @return The function’s result.
   * @throws EvaluationException If an error occured during {@link Node} evaluation.
   * @throws ArithmeticException If a math error occured.
   */
  @Override
  public double evaluate(final Scope scope) throws EvaluationException, ArithmeticException {
    return scope.getFunction(this.name).evaluate(scope, this.operands.stream()
        .map(node -> node.evaluate(scope)).collect(Collectors.toList()));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    FunctionNode that = (FunctionNode) o;
    return this.name.equals(that.name) && this.operands.equals(that.operands);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.name, this.operands);
  }

  @Override
  public String toString() {
    return String.format("%s(%s)", this.name, this.operands.stream().map(Object::toString).collect(Collectors.joining(", ")));
  }
}
