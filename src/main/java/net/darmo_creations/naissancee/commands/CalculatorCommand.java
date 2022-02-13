package net.darmo_creations.naissancee.commands;

import net.darmo_creations.naissancee.NaissanceE;
import net.darmo_creations.naissancee.calculator.Calculator;
import net.darmo_creations.naissancee.calculator.Function;
import net.darmo_creations.naissancee.calculator.exceptions.*;
import net.darmo_creations.naissancee.calculator.nodes.StatementResult;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
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
  private static final Style BUILTINS_STYLE = new Style().setColor(TextFormatting.AQUA);

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
    return "commands.calculator.usage";
  }

  @Override
  public int getRequiredPermissionLevel() {
    return 0; // Any player and command blocks can use this command
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    if (args.length == 0) {
      throw new WrongUsageException("commands.calculator.usage");
    }

    Optional<Entity> entity = Optional.ofNullable(sender.getCommandSenderEntity());
    Calculator calculator;
    boolean useGlobal = args[0].equals("global");
    String username;

    if (entity.isPresent() && entity.get() instanceof EntityPlayer && !useGlobal) {
      EntityPlayer player = (EntityPlayer) entity.get();
      username = player.getGameProfile().getName();
      calculator = NaissanceE.CALCULATORS_MANAGER.getOrCreatePlayerData(player);
    } else {
      calculator = NaissanceE.CALCULATORS_MANAGER.getGlobalData();
      username = TextFormatting.ITALIC + "<Global>" + TextFormatting.RESET;
      if (useGlobal) {
        args = Arrays.copyOfRange(args, 1, args.length);
      }
    }

    if (args.length == 1 && args[0].equals("reset")) {
      this.reset(sender, username, calculator);
    } else if (args.length == 3 && args[0].equals("delete") && !args[1].equals(":=")) {
      this.delete(sender, args, username, calculator);
    } else if ((args.length == 3) && args[0].equals("list")) {
      this.list(sender, args, username, calculator);
    } else {
      this.evaluate(sender, args, calculator);
    }
  }

  @Override
  public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
    if (args.length == 1) {
      return getListOfStringsMatchingLastWord(args, Arrays.asList("delete", "list", "reset", "global"));

    } else if (args.length == 2 && args[0].equals("global")) {
      return getListOfStringsMatchingLastWord(args, Arrays.asList("delete", "list", "reset"));

    } else {
      Optional<Entity> entity = Optional.ofNullable(sender.getCommandSenderEntity());
      Calculator calculator;
      boolean useGlobal = args[0].equals("global");

      if (entity.isPresent() && entity.get() instanceof EntityPlayer && !useGlobal) {
        EntityPlayer player = (EntityPlayer) entity.get();
        calculator = NaissanceE.CALCULATORS_MANAGER.getOrCreatePlayerData(player);
      } else {
        calculator = NaissanceE.CALCULATORS_MANAGER.getGlobalData();
        if (useGlobal) {
          args = Arrays.copyOfRange(args, 1, args.length);
        }
      }

      if (args[0].equals("list")) {
        if (args.length == 2) {
          return getListOfStringsMatchingLastWord(args,
              Arrays.stream(ListType.values()).map(e -> e.name().toLowerCase()).collect(Collectors.toList()));
        } else if (args.length == 3 && ListType.getType(args[1]).isPresent()) {
          return getListOfStringsMatchingLastWord(args, Arrays.asList("variables", "functions"));
        }

      } else if (args[0].equals("delete")) {
        if (args.length == 2) {
          return getListOfStringsMatchingLastWord(args, Arrays.asList("variable", "function"));
        } else if (args.length == 3 && args[1].equals("variable")) {
          return getListOfStringsMatchingLastWord(args,
              calculator.getVariables().keySet().stream().sorted().collect(Collectors.toList()));
        } else if (args.length == 3 && args[1].equals("function")) {
          return getListOfStringsMatchingLastWord(args,
              calculator.getFunctions().stream().map(Function::getName).sorted().collect(Collectors.toList()));
        }
      }
    }

    return Collections.emptyList();
  }

  /**
   * Reset the given calculator.
   *
   * @param sender     Player who ran the command.
   * @param username   Name of the user associated to the calculator.
   * @param calculator The calculator.
   */
  private void reset(final ICommandSender sender, final String username, Calculator calculator) {
    calculator.reset();
    notifyCommandListener(sender, this, "commands.calculator.feedback.reset", username);
  }

  /**
   * Delete the given variable or function.
   *
   * @param sender     Player who ran the command.
   * @param args       Command’s arguments.
   * @param username   Name of the user associated to the calculator.
   * @param calculator The calculator.
   * @throws CommandException If arguments are incorrect or the variable/function does not exist or is builtin.
   */
  private void delete(final ICommandSender sender, final String[] args, final String username, Calculator calculator) throws CommandException {
    boolean function = args[1].equals("function");
    boolean variable = args[1].equals("variable");
    String id = args[2];
    try {
      if (variable) {
        calculator.deleteVariable(id);
      } else if (function) {
        calculator.deleteFunction(id);
      } else {
        throw new WrongUsageException("commands.calculator.usage");
      }
    } catch (UndefinedVariableException e) {
      throw new CommandException("commands.calculator.error.undefined_variable", e.getMessage());
    } catch (BuiltinConstantDeletionAttemptException e) {
      throw new CommandException("commands.calculator.error.delete_builtin_constant", e.getMessage());
    } catch (UndefinedFunctionException e) {
      throw new CommandException("commands.calculator.error.undefined_function", e.getMessage());
    } catch (BuiltinFunctionDeletionAttemptException e) {
      throw new CommandException("commands.calculator.error.delete_builtin_function", e.getMessage());
    }
    String key = String.format("commands.calculator.feedback.%s_deleted", function ? "function" : "variable");
    notifyCommandListener(sender, this, key, id, username);
  }

  /**
   * List variables or functions.
   *
   * @param sender     Player who ran the command.
   * @param args       Command’s arguments.
   * @param username   Name of the user associated to the calculator.
   * @param calculator The calculator.
   * @throws CommandException If arguments are incorrect.
   */
  private void list(final ICommandSender sender, final String[] args, final String username, final Calculator calculator) throws CommandException {
    Optional<ListType> type = ListType.getType(args[1]);

    if (!type.isPresent()) {
      throw new WrongUsageException("commands.calculator.usage");
    }

    List<ITextComponent> list = new ArrayList<>();

    if (args[2].equals("variables")) {
      switch (type.get()) {
        case ALL:
          list = listVariables(calculator.getBuiltinConstants(), true);
          list.addAll(listVariables(calculator.getVariables(), false));
          break;
        case CUSTOM:
          list = listVariables(calculator.getVariables(), false);
          break;
        case BUILTIN:
          list = listVariables(calculator.getBuiltinConstants(), true);
          break;
      }

    } else if (args[2].equals("functions")) {
      switch (type.get()) {
        case ALL:
          list = listFunctions(calculator.getBuiltinFunctions(), true);
          list.addAll(listFunctions(calculator.getFunctions(), false));
          break;
        case CUSTOM:
          list = listFunctions(calculator.getFunctions(), false);
          break;
        case BUILTIN:
          list = listFunctions(calculator.getBuiltinFunctions(), true);
          break;
      }

    } else {
      throw new WrongUsageException("commands.calculator.usage");
    }

    ITextComponent message = TextComponentHelper.createComponentTranslation(
        sender, String.format("commands.calculator.feedback.list.%s_%s", args[1], args[2]), username);
    for (ITextComponent str : list) {
      message.appendText("\n").appendSibling(str);
    }
    sender.sendMessage(message);
  }

  /**
   * Evaluates a statement then displays its result. If the statement is a single expression,
   * its value is stored in a variable named “_“.
   *
   * @param sender     Player who ran the command.
   * @param args       Command’s arguments.
   * @param calculator The calculator.
   * @throws CommandException If any syntax, math or evaluation error occurs.
   */
  private void evaluate(final ICommandSender sender, final String[] args, Calculator calculator) throws CommandException {
    String expression = String.join(" ", args);
    StatementResult result = null;
    CommandException exception = null;

    try {
      result = calculator.evaluate(expression);
    } catch (MaxDefinitionsException e) {
      exception = new CommandException("commands.calculator.error.max_declaration_quota_reached", e.getNumber());
    } catch (SyntaxErrorException e) {
      exception = new CommandException("commands.calculator.error.syntax_error");
    } catch (UndefinedVariableException e) {
      exception = new CommandException("commands.calculator.error.undefined_variable", e.getMessage());
    } catch (UndefinedFunctionException e) {
      exception = new CommandException("commands.calculator.error.undefined_function", e.getMessage());
    } catch (InvalidFunctionArguments e) {
      exception = new CommandException("commands.calculator.error.invalid_function_params", e.getFunctionName(), e.getExpected(), e.getActual());
    } catch (MaxDepthReachedException e) {
      exception = new CommandException("commands.calculator.error.max_depth_reached", e.getDepth());
    } catch (ArithmeticException e) {
      exception = new CommandException("commands.calculator.error.math_error", e.getMessage());
    }

    // Display what the player just typed
    sender.sendMessage(new TextComponentString("$ " + expression));
    if (exception != null) {
      throw exception;
    } else {
      sender.sendMessage(new TextComponentString(result.getStatus())
          .setStyle(new Style().setColor(TextFormatting.GREEN)));
      // Store result in a special variable
      result.getValue().ifPresent(v -> calculator.setVariable("_", v));
    }
  }

  /**
   * Generate a list of text components for a mapping of variables.
   *
   * @param variables The variables to format.
   * @param builtin   Whether the variables are builtin; modifies styling.
   * @return The list of text components.
   */
  private static List<ITextComponent> listVariables(final Map<String, Double> variables, final boolean builtin) {
    return variables.entrySet().stream()
        .sorted(Map.Entry.comparingByKey())
        .map(e -> getTextComponent(String.format("%s = %f", e.getKey(), e.getValue()), builtin))
        .collect(Collectors.toList());
  }

  /**
   * Generate a list of text components for a list of functions.
   *
   * @param functions The functions to format.
   * @param builtin   Whether the functions are builtin; modifies styling.
   * @return The list of text components.
   */
  private static List<ITextComponent> listFunctions(final List<Function> functions, final boolean builtin) {
    return functions.stream()
        .sorted(Comparator.comparing(Function::getName))
        .map(f -> getTextComponent(f.toString(), builtin))
        .collect(Collectors.toList());
  }

  /**
   * Return the text component for the given text.
   *
   * @param text    Text to format.
   * @param builtin Whether to apply the “builtin” style.
   * @return The text component.
   */
  private static ITextComponent getTextComponent(final String text, final boolean builtin) {
    TextComponentString component = new TextComponentString(text);
    if (builtin) {
      component.setStyle(BUILTINS_STYLE);
    }
    return component;
  }

  /**
   * Options for the “list” parameter.
   */
  private enum ListType {
    ALL, CUSTOM, BUILTIN;

    /**
     * Return a list type for the given text.
     */
    public static Optional<ListType> getType(final String name) {
      for (ListType t : values()) {
        if (t.name().toLowerCase().equals(name)) {
          return Optional.of(t);
        }
      }
      return Optional.empty();
    }
  }
}
