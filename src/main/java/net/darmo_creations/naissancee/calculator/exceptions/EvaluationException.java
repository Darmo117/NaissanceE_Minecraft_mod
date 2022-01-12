package net.darmo_creations.naissancee.calculator.exceptions;

/**
 * Base class for exception that may occur during calculator statement evaluation.
 */
public class EvaluationException extends RuntimeException {
  public EvaluationException(final String message) {
    super(message);
  }
}
