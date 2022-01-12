package net.darmo_creations.naissancee.commands;

import net.darmo_creations.naissancee.calculator.Calculator;
import net.darmo_creations.naissancee.calculator.Function;
import net.darmo_creations.naissancee.calculator.exceptions.*;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.server.command.TextComponentHelper;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A command that lets users perform mathematical computations, define variables and simple functions.
 * A maximum of 100 variables and 100 functions can be defined by each user and in the global calculator.
 * <p>
 * A new calculator instance is created for each entity that executes the command.
 */
public class CalculatorCommand extends CommandBase {
  public static final int MAX_VARS_PER_PLAYER = 100;
  /**
   * Global calculator, usable by command blocks.
   */
  public static final Calculator GLOBAL_CALCULATOR = new Calculator(MAX_VARS_PER_PLAYER);
  /**
   * Calculators associated to each player that ran this command.
   */
  private static final Map<UUID, Calculator> CALCULATORS = new HashMap<>();

  /**
   * Return a calculator for the given player. Create a new instance if none were found.
   *
   * @param uuid Player’s UUID.
   * @return The calculator.
   */
  public static Calculator getCalculatorForPlayer(UUID uuid) {
    if (!CALCULATORS.containsKey(uuid)) {
      CALCULATORS.put(uuid, new Calculator(MAX_VARS_PER_PLAYER));
    }
    return CALCULATORS.get(uuid);
  }

  @Override
  public String getName() {
    return "calculator";
  }

  @Override
  public List<String> getAliases() {
    return Collections.singletonList("c");
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return String.format("commands.%s.usage", this.getName());
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    Optional<Entity> entity = Optional.ofNullable(sender.getCommandSenderEntity());
    Calculator calculator;
    boolean useGlobal = args[0].equals("global");

    if (entity.isPresent() && !useGlobal) {
      calculator = getCalculatorForPlayer(entity.get().getUniqueID());
    } else {
      calculator = GLOBAL_CALCULATOR;
      if (useGlobal) {
        args = Arrays.copyOfRange(args, 1, args.length);
      }
    }

    if (args.length == 1 && args[0].equals("reset")) {
      this.reset(sender, calculator);
    } else if (args.length == 3 && args[0].equals("delete") && !args[1].equals(":=")) {
      this.delete(sender, args, calculator);
    } else if ((args.length == 3) && args[0].equals("list")) {
      this.list(sender, args, calculator);
    } else {
      this.evaluate(sender, args, calculator);
    }
  }

  /**
   * Reset the given calculator.
   *
   * @param sender     Player who ran the command.
   * @param calculator The calculator.
   */
  private void reset(final ICommandSender sender, Calculator calculator) {
    calculator.reset();
    sender.sendMessage(TextComponentHelper.createComponentTranslation(sender, "commands.calculator.feedback.reset")
        .setStyle(new Style().setColor(TextFormatting.DARK_GREEN)));
  }

  /**
   * Delete the given variable or function.
   *
   * @param sender     Player who ran the command.
   * @param args       Command’s arguments.
   * @param calculator The calculator.
   * @throws CommandException If arguments are incorrect or the variable/function does not exist or is builtin.
   */
  private void delete(final ICommandSender sender, final String[] args, Calculator calculator) throws CommandException {
    boolean function = args[1].equals("function");
    boolean variable = args[1].equals("variable");
    String id = args[2];
    try {
      if (variable) {
        calculator.deleteVariable(id);
      } else if (function) {
        calculator.deleteFunction(id);
      } else {
        throw new CommandException(this.translate(sender, "invalid_delete_param"));
      }
    } catch (UndefinedVariableException e) {
      throw new CommandException(this.translate(sender, "commands.calculator.error.undefined_variable", e.getMessage()));
    } catch (BuiltinConstantDeletionAttemptException e) {
      throw new CommandException(this.translate(sender, "commands.calculator.error.delete_builtin_constant", e.getMessage()));
    } catch (UndefinedFunctionException e) {
      throw new CommandException(this.translate(sender, "commands.calculator.error.undefined_function", e.getMessage()));
    } catch (BuiltinFunctionDeletionAttemptException e) {
      throw new CommandException(this.translate(sender, "commands.calculator.error.delete_builtin_function", e.getMessage()));
    }
    String key = String.format("commands.calculator.feedback.%s_deleted", function ? "function" : "variable");
    sender.sendMessage(TextComponentHelper.createComponentTranslation(sender, key, id)
        .setStyle(new Style().setColor(TextFormatting.DARK_GREEN)));
  }

  /**
   * List variables or functions.
   *
   * @param sender     Player who ran the command.
   * @param args       Command’s arguments.
   * @param calculator The calculator.
   * @throws CommandException If arguments are incorrect.
   */
  private void list(final ICommandSender sender, final String[] args, final Calculator calculator) throws CommandException {
    ListType type = ListType.getType(args[1]);

    if (type == null) {
      throw new CommandException(this.translate(sender, "commands.calculator.error.invalid_list_param"));
    }

    List<ITextComponent> list = new ArrayList<>();

    if (args[2].equals("variables")) {
      switch (type) {
        case ALL:
          list = this.listVariables(calculator.getBuiltinConstants(), true);
          list.addAll(this.listVariables(calculator.getVariables(), false));
          break;
        case MY:
          list = this.listVariables(calculator.getVariables(), false);
          break;
        case BUILTIN:
          list = this.listVariables(calculator.getBuiltinConstants(), true);
          break;
      }

    } else if (args[2].equals("functions")) {
      switch (type) {
        case ALL:
          list = this.listFunctions(calculator.getBuiltinFunctions(), true);
          list.addAll(this.listFunctions(calculator.getFunctions(), false));
          break;
        case MY:
          list = this.listFunctions(calculator.getFunctions(), false);
          break;
        case BUILTIN:
          list = this.listFunctions(calculator.getBuiltinFunctions(), true);
          break;
      }

    } else {
      throw new CommandException(this.translate(sender, "commands.calculator.error.invalid_list_param"));
    }

    ITextComponent message = TextComponentHelper.createComponentTranslation(
        sender, String.format("commands.calculator.feedback.list.%s_%s", args[1], args[2]));
    for (ITextComponent str : list) {
      message.appendText("\n").appendSibling(str);
    }
    sender.sendMessage(message);
  }

  /**
   * Evaluates an expression then displays its result.
   *
   * @param sender     Player who ran the command.
   * @param args       Command’s arguments.
   * @param calculator The calculator.
   * @throws CommandException If any syntax, math or evaluation error occurs.
   */
  private void evaluate(final ICommandSender sender, final String[] args, Calculator calculator) throws CommandException {
    String expression = String.join(" ", args);
    Optional<String> result = Optional.empty();
    String errorMessage = null;

    try {
      result = Optional.of(calculator.evaluate(expression));
    } catch (MaxDefinitionsException e) {
      errorMessage = this.translate(sender, "commands.calculator.error.max_declare_quota_reached", e.getNumber());
    } catch (SyntaxErrorException e) {
      errorMessage = this.translate(sender, "commands.calculator.error.syntax_error");
    } catch (UndefinedVariableException e) {
      errorMessage = this.translate(sender, "commands.calculator.error.undefined_variable", e.getMessage());
    } catch (UndefinedFunctionException e) {
      errorMessage = this.translate(sender, "commands.calculator.error.undefined_function", e.getMessage());
    } catch (InvalidFunctionArguments e) {
      errorMessage = this.translate(sender, "commands.calculator.error.invalid_function_params", e.getFunctionName(), e.getExpected(), e.getActual());
    } catch (MaxDepthReachedException e) {
      errorMessage = this.translate(sender, "commands.calculator.error.max_depth_reached", e.getDepth());
    } catch (ArithmeticException e) {
      errorMessage = this.translate(sender, "commands.calculator.error.math_error", e.getMessage());
    }

    // Display what the player just typed
    sender.sendMessage(new TextComponentString("$ " + expression));
    if (errorMessage != null) {
      throw new CommandException(errorMessage);
    } else {
      result.ifPresent(operationResult -> sender.sendMessage(new TextComponentString(operationResult)
          .setStyle(new Style().setColor(TextFormatting.GREEN))));
    }
  }

  /**
   * Translate the given key.
   *
   * @param sender The player who ran the command.
   * @param key    Key to translate.
   * @param args   Optional formatting arguments.
   * @return The translated text.
   */
  private String translate(ICommandSender sender, String key, Object... args) {
    return TextComponentHelper.createComponentTranslation(sender, key, args).getUnformattedText();
  }

  /**
   * Generate a list of text components for a mapping of variables.
   *
   * @param variables The variables to format.
   * @param builtin   Whether the variables are builtin; modifies styling.
   * @return The list of text components.
   */
  private List<ITextComponent> listVariables(final Map<String, Double> variables, final boolean builtin) {
    return variables.entrySet().stream()
        .sorted(Map.Entry.comparingByKey())
        .map(e -> this.getTextComponent(String.format("%s = %f", e.getKey(), e.getValue()), builtin))
        .collect(Collectors.toList());
  }

  /**
   * Generate a list of text components for a list of functions.
   *
   * @param functions The functions to format.
   * @param builtin   Whether the functions are builtin; modifies styling.
   * @return The list of text components.
   */
  private List<ITextComponent> listFunctions(final List<Function> functions, final boolean builtin) {
    return functions.stream()
        .sorted(Comparator.comparing(Function::getName))
        .map(f -> this.getTextComponent(f.toString(), builtin))
        .collect(Collectors.toList());
  }

  /**
   * Return the text component for the given text.
   *
   * @param text    Text to format.
   * @param builtin Whether to apply the “builtin” style.
   * @return The text component.
   */
  private ITextComponent getTextComponent(final String text, final boolean builtin) {
    TextComponentString component = new TextComponentString(text);
    if (builtin) {
      component.setStyle(BUILTIN_STYLE);
    }
    return component;
  }

  private static final Style BUILTIN_STYLE = new Style().setColor(TextFormatting.AQUA);

  /**
   * Options for the “list” parameter.
   */
  private enum ListType {
    ALL, MY, BUILTIN;

    /**
     * Return a list type for the given text.
     */
    public static ListType getType(final String name) {
      for (ListType t : values()) {
        if (t.name().toLowerCase().equals(name)) {
          return t;
        }
      }
      return null;
    }
  }
}
