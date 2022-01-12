package net.darmo_creations.naissancee.calculator.nodes.expr;

import net.darmo_creations.naissancee.calculator.Scope;

import java.util.Objects;

/**
 * A {@link Node} representing a number.
 */
public class NumberNode extends Node {
  private final double value;

  /**
   * Create a number {@link Node}.
   *
   * @param value Number value.
   */
  public NumberNode(final double value) {
    this.value = value;
  }

  /**
   * Return the value of this node.
   *
   * @return The associated number.
   */
  @Override
  public double evaluate(final Scope scope) {
    return this.value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    return Double.compare(((NumberNode) o).value, this.value) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.value);
  }

  @Override
  public String toString() {
    return "" + this.value;
  }
}
