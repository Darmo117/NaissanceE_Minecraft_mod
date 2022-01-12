package net.darmo_creations.naissancee.calculator.nodes.expr;

import net.darmo_creations.naissancee.calculator.Scope;
import net.darmo_creations.naissancee.calculator.exceptions.UndefinedVariableException;

import java.util.Objects;

/**
 * A {@link Node} representing a variable.
 */
public class VariableNode extends Node {
  private final String name;

  /**
   * Create a variable {@link Node}.
   *
   * @param name Variable’s name.
   */
  public VariableNode(final String name) {
    this.name = Objects.requireNonNull(name);
  }

  /**
   * Return the value of the associated variable.
   *
   * @return The variable’s value.
   * @throws UndefinedVariableException If no variable or constant with this name exists in the given scope.
   */
  @Override
  public double evaluate(final Scope scope) throws UndefinedVariableException {
    return scope.getVariable(this.name);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    return this.name.equals(((VariableNode) o).name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.name);
  }

  @Override
  public String toString() {
    return this.name;
  }
}
