package net.darmo_creations.naissancee.calculator;

import net.darmo_creations.naissancee.calculator.antlr4_parser.CalculatorBaseVisitor;
import net.darmo_creations.naissancee.calculator.antlr4_parser.CalculatorParser;
import net.darmo_creations.naissancee.calculator.nodes.expr.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * Visitor for “exp” grammar rule. Generates a {@link Node} tree.
 */
public class ExpressionVisitor extends CalculatorBaseVisitor<Node> {
  /**
   * Maps operator symbols to the corresponding {@link Node} provider.
   */
  public static final Map<String, BiFunction<Node, Node, OperatorNode>> OPERATORS = new HashMap<>();

  static {
    OPERATORS.put("*", MultiplicationOperatorNode::new);
    OPERATORS.put("/", DivisionOperatorNode::new);
    OPERATORS.put("%", ModuloOperatorNode::new);
    OPERATORS.put("+", AdditionOperatorNode::new);
    OPERATORS.put("-", SubtractionOperatorNode::new);
    OPERATORS.put("^", PowerOperatorNode::new);
  }

  @Override
  public Node visitParentheses(CalculatorParser.ParenthesesContext ctx) {
    return this.visit(ctx.expr);
  }

  @Override
  public Node visitOperator(CalculatorParser.OperatorContext ctx) {
    Node left = this.visit(ctx.left);
    Node right = this.visit(ctx.right);
    String operator = ctx.operator.getText();
    return OPERATORS.get(operator).apply(left, right);
  }

  @Override
  public Node visitMinus(CalculatorParser.MinusContext ctx) {
    return new MinusOperatorNode(this.visit(ctx.operand));
  }

  @Override
  public Node visitPlus(CalculatorParser.PlusContext ctx) {
    return this.visit(ctx.operand);
  }

  @Override
  public Node visitFunction(CalculatorParser.FunctionContext ctx) {
    String name = ctx.name.getText();
    List<Node> arguments = ctx.exp().stream().map(this::visit).collect(Collectors.toList());
    return new FunctionNode(name, arguments);
  }

  @Override
  public Node visitNumber(CalculatorParser.NumberContext ctx) {
    return new NumberNode(Double.parseDouble(ctx.getText()));
  }

  @Override
  public Node visitVariable(CalculatorParser.VariableContext ctx) {
    return new VariableNode(ctx.getText());
  }
}
