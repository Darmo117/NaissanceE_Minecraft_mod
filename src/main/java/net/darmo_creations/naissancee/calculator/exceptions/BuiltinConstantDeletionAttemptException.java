package net.darmo_creations.naissancee.calculator.exceptions;

/**
 * Error raised when there was an attempt to delete a builtin constant.
 */
public class BuiltinConstantDeletionAttemptException extends EvaluationException {
  /**
   * Create an exception.
   *
   * @param constantName Constant’s name.
   */
  public BuiltinConstantDeletionAttemptException(final String constantName) {
    super(constantName);
  }
}
