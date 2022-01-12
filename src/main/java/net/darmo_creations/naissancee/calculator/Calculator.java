package net.darmo_creations.naissancee.calculator;

import net.darmo_creations.naissancee.calculator.exceptions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A calculator that can parse and evaluate simple mathematical expressions and declare variables and functions.
 */
public class Calculator {
  private final Scope scope;

  /**
   * Create a calculator.
   *
   * @param maxAllowedDefinitions The maximum allowed number of variables and functions.
   */
  public Calculator(final int maxAllowedDefinitions) {
    this.scope = new Scope(maxAllowedDefinitions);
  }

  /**
   * Return a mapping of all user-defined variables.
   */
  public Map<String, Double> getVariables() {
    return this.scope.getVariables();
  }

  /**
   * Return a mapping of all builtin constants.
   */
  public Map<String, Double> getBuiltinConstants() {
    return this.scope.getBuiltinConstants();
  }

  /**
   * Return a list of all user-defined functions.
   */
  public List<Function> getFunctions() {
    return new ArrayList<>(this.scope.getFunctions().values());
  }

  /**
   * Return a list of all builtin functions.
   */
  public List<Function> getBuiltinFunctions() {
    return new ArrayList<>(this.scope.getBuiltinFunctions().values());
  }

  /**
   * Evaluate an expression then return its result.
   *
   * @param expression The expression to parse and evaluate.
   * @return The value of the expression.
   * @throws ArithmeticException If any math error occurs.
   */
  public String evaluate(final String expression)
      throws SyntaxErrorException, ArithmeticException, EvaluationException {
    return "" + Parser.parse(expression).execute(this.scope);
  }

  /**
   * Set the value of a variable.
   *
   * @param name  Variable’s name.
   * @param value Variable’s value.
   * @return Variable’s previous value if it was already defined.
   * @throws MaxDefinitionsException If the maximum quota of variables definitions has been reached.
   */
  @SuppressWarnings("UnusedReturnValue")
  public Optional<Double> setVariable(String name, double value) throws MaxDefinitionsException {
    return this.scope.setVariable(name, value);
  }

  /**
   * Delete the variable that has the given name.
   *
   * @param name Name of the variable.
   * @return The value of the variable.
   * @throws UndefinedVariableException              If no variable with this name exists.
   * @throws BuiltinConstantDeletionAttemptException If the identifier corresponds to a builtin variable.
   */
  @SuppressWarnings("UnusedReturnValue")
  public double deleteVariable(String name)
      throws UndefinedVariableException, BuiltinConstantDeletionAttemptException {
    return this.scope.deleteVariable(name);
  }

  /**
   * Delete the function that has the given name.
   *
   * @param name Name of the function.
   * @return The function.
   * @throws UndefinedVariableException              If no function with this name exists.
   * @throws BuiltinFunctionDeletionAttemptException If the identifier corresponds to a builtin function.
   */
  @SuppressWarnings("UnusedReturnValue")
  public Function deleteFunction(String name)
      throws UndefinedVariableException, BuiltinFunctionDeletionAttemptException {
    return this.scope.deleteFunction(name);
  }

  /**
   * Reset the context of this calculator, i.e. delete all user-defined variables and functions.
   */
  public void reset() {
    this.scope.reset();
  }
}
