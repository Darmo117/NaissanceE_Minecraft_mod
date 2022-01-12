package net.darmo_creations.naissancee.calculator.exceptions;

import net.darmo_creations.naissancee.calculator.Calculator;

/**
 * Error raised when a syntax error occurs while an expression is being parsed by the {@link Calculator}
 * and {@link net.darmo_creations.naissancee.calculator.Parser}.
 */
public class SyntaxErrorException extends RuntimeException {
  public SyntaxErrorException(String message) {
    super(message);
  }
}
