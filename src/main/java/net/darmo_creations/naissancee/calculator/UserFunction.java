package net.darmo_creations.naissancee.calculator;

import net.darmo_creations.naissancee.calculator.exceptions.MaxDepthReachedException;
import net.darmo_creations.naissancee.calculator.nodes.expr.Node;

import java.util.List;
import java.util.Objects;

/**
 * User functions are functions that can be defined by users.
 */
public class UserFunction extends Function {
  private final Node node;

  /**
   * Create a function with the given names, parameters and content.
   *
   * @param name           Function’s name.
   * @param parameterNames Function’s parameter names.
   * @param node           Function’s node tree.
   */
  public UserFunction(final String name, final List<String> parameterNames, final Node node) {
    super(name, parameterNames);
    this.node = Objects.requireNonNull(node);
  }

  @Override
  protected double evaluateImpl(final Scope scope) {
    return this.node.evaluate(scope);
  }

  @Override
  public String toString() {
    return String.format("%s(%s) -> %s", this.getName(), String.join(", ", this.getParameterNames()), this.node);
  }
}
