package net.darmo_creations.naissancee.calculator.nodes;

import net.darmo_creations.naissancee.calculator.Scope;

/**
 * A statement can be executed in a given scope.
 */
public abstract class Statement {
  /**
   * Executes this statement.
   *
   * @param scope Scope to use.
   * @return The result of the statement.
   */
  public abstract String execute(Scope scope);

  @Override
  public abstract boolean equals(Object o);

  @Override
  public abstract int hashCode();

  @Override
  public abstract String toString();
}
