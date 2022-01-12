package net.darmo_creations.naissancee.calculator.exceptions;

/**
 * Error raised when there was an attempt to delete a variable defined in parent scopes.
 */
public class OutOfScopeVariableDeletionAttemptException extends EvaluationException {
  /**
   * Create an exception.
   *
   * @param variableName Constant’s name.
   */
  public OutOfScopeVariableDeletionAttemptException(final String variableName) {
    super(variableName);
  }
}
