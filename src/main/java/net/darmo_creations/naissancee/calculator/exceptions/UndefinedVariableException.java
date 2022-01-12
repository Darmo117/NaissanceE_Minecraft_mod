package net.darmo_creations.naissancee.calculator.exceptions;

import net.darmo_creations.naissancee.calculator.Calculator;

/**
 * Error raised when an undefined variable is encountered
 * while an expression is being evaluated by the {@link Calculator}.
 */
public class UndefinedVariableException extends EvaluationException {
  public UndefinedVariableException(String identifier) {
    super(identifier);
  }
}
