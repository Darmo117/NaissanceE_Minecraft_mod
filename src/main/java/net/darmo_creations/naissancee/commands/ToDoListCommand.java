package net.darmo_creations.naissancee.commands;

import net.darmo_creations.naissancee.NaissanceE;
import net.darmo_creations.naissancee.todo_list.ToDoList;
import net.darmo_creations.naissancee.todo_list.ToDoListItem;
import net.minecraft.command.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A command that lets users edit to-do lists to keep track of thing to do.
 * A maximum of 100 items are allowed per list.
 * <p>
 * A new list is created for each player that executes the command.
 */
public class ToDoListCommand extends CommandBase {
  @Override
  public String getName() {
    return "todo";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "commands.todo.usage";
  }

  @Override
  public int getRequiredPermissionLevel() {
    return 0; // Any player and command blocks can use this command
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    if (args.length == 0 || args.length == 1 && args[0].equals("global")) {
      throw new WrongUsageException("commands.todo.usage");
    }

    Optional<Entity> entity = Optional.ofNullable(sender.getCommandSenderEntity());
    ToDoList toDoList;
    boolean useGlobal = args[0].equals("global");
    String username;

    if (entity.isPresent() && entity.get() instanceof EntityPlayer && !useGlobal) {
      EntityPlayer player = (EntityPlayer) entity.get();
      username = player.getGameProfile().getName();
      toDoList = NaissanceE.TODO_LISTS_MANAGER.getOrCreatePlayerData(player);
    } else {
      toDoList = NaissanceE.TODO_LISTS_MANAGER.getGlobalData();
      username = TextFormatting.ITALIC + "<Global>" + TextFormatting.RESET;
      if (useGlobal) {
        args = Arrays.copyOfRange(args, 1, args.length);
      }
    }

    Option option = Option.getOption(args[0]).orElseThrow(() -> new WrongUsageException("commands.todo.usage"));
    String[] remainingArgs = Arrays.copyOfRange(args, 1, args.length);
    boolean check = false;

    switch (option) {
      case CLEAR:
        if (args.length == 1) {
          this.clearList(sender, username, toDoList);
          return;
        }
        break;

      case ADD:
        if (args.length >= 2) {
          this.addItem(sender, remainingArgs, username, toDoList);
          return;
        }
        break;

      case REMOVE:
        if (args.length == 2) {
          this.removeItems(sender, remainingArgs, username, toDoList);
          return;
        }
        break;

      case EDIT:
        if (args.length >= 3) {
          this.setItemText(sender, remainingArgs, username, toDoList);
          return;
        }
        break;

      case CHECK:
        check = true;
      case UNCHECK:
        if (args.length == 2) {
          this.checkItem(sender, remainingArgs, username, toDoList, check);
          return;
        }
        break;

      case GET:
        if (args.length == 2) {
          this.getListOption(sender, remainingArgs, toDoList);
          return;
        }
        break;

      case SET:
        if (args.length == 3) {
          this.setListOption(sender, remainingArgs, toDoList);
          return;
        }
        break;
    }
    throw new WrongUsageException("commands.todo.usage");
  }

  @Override
  public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
    if (args.length == 1) {
      List<String> list = Arrays.stream(Option.values()).map(Option::getName).collect(Collectors.toList());
      list.add("global");
      return getListOfStringsMatchingLastWord(args, list);

    } else if (args.length == 2 && args[0].equals("global")) {
      return getListOfStringsMatchingLastWord(args,
          Arrays.stream(Option.values()).map(Option::getName).collect(Collectors.toList()));

    } else {
      Optional<Entity> entity = Optional.ofNullable(sender.getCommandSenderEntity());
      boolean useGlobal = args[0].equals("global");

      if (!entity.isPresent() || !(entity.get() instanceof EntityPlayer) || useGlobal) {
        if (useGlobal) {
          args = Arrays.copyOfRange(args, 1, args.length);
        }
      }

      if (args.length == 2 && (args[0].equals("get") || args[0].equals("set"))) {
        return getListOfStringsMatchingLastWord(args,
            Arrays.stream(ListOption.values()).map(ListOption::getName).collect(Collectors.toList()));
      } else if (args.length == 3 && args[0].equals("set")) {
        return getListOfStringsMatchingLastWord(args,
            ListOption.getOption(args[1]).map(ListOption::getSuggestedValues).orElseGet(Collections::emptyList));
      } else if (args.length == 2 && args[0].equals("remove")) {
        return getListOfStringsMatchingLastWord(args, Collections.singletonList("checked"));
      }
    }

    return Collections.emptyList();
  }

  /**
   * Reset the given to-do list.
   *
   * @param sender   Player who ran the command.
   * @param username Name of the user associated to the calculator.
   * @param list     The list.
   */
  private void clearList(final ICommandSender sender, final String username, ToDoList list) {
    list.clear();
    notifyCommandListener(sender, this, "commands.todo.feedback.cleared", username);
  }

  /**
   * Add an item to the given list.
   *
   * @param sender   Player who ran the command.
   * @param args     The command arguments.
   * @param username Name of the user associated to the calculator.
   * @param list     The list.
   */
  private void addItem(final ICommandSender sender, final String[] args, final String username, ToDoList list) throws CommandException {
    String text = String.join(" ", args).trim();
    if (text.equals("")) {
      throw new WrongUsageException("command.todo.usage");
    }
    boolean added = list.add(new ToDoListItem(text));
    if (added) {
      if (!list.isVisible()) {
        notifyCommandListener(sender, this, "commands.todo.feedback.item_added",
            TextFormatting.ITALIC + text + TextFormatting.RESET, username);
      }
    } else {
      throw new CommandException("commands.todo.error.list_full", username);
    }
  }

  /**
   * Remove an item from the given list.
   *
   * @param sender   Player who ran the command.
   * @param args     The command arguments.
   * @param username Name of the user associated to the calculator.
   * @param list     The list.
   */
  private void removeItems(final ICommandSender sender, final String[] args, final String username, ToDoList list)
      throws NumberInvalidException {
    if (args[0].equals("checked")) {
      int removed = list.deleteCheckedItems();
      if (removed != 0) {
        notifyCommandListener(sender, this, "commands.todo.feedback.checked_items_removed", removed, username);
      } else {
        notifyCommandListener(sender, this, "commands.todo.feedback.no_checked_items_removed");
      }

    } else {
      int index = parseInt(args[0]) - 1;
      ToDoListItem item;
      try {
        item = list.remove(index);
      } catch (IndexOutOfBoundsException e) {
        throw new NumberInvalidException("commands.todo.error.out_of_bounds");
      }
      if (!list.isVisible()) {
        notifyCommandListener(sender, this, "commands.todo.feedback.item_removed",
            TextFormatting.ITALIC + item.getText() + TextFormatting.RESET, username);
      }
    }
  }

  /**
   * Set the text of the given item.
   *
   * @param sender   Player who ran the command.
   * @param args     The command arguments.
   * @param username Name of the user associated to the calculator.
   * @param list     The list.
   */
  private void setItemText(final ICommandSender sender, final String[] args, final String username, ToDoList list)
      throws NumberInvalidException {
    int index = parseInt(args[0]) - 1;
    String text = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
    try {
      list.setText(index, text);
    } catch (IndexOutOfBoundsException e) {
      throw new NumberInvalidException("commands.todo.error.out_of_bounds");
    }
    if (!list.isVisible()) {
      notifyCommandListener(sender, this, "commands.todo.feedback.item_text_changed",
          index + 1, TextFormatting.ITALIC + text + TextFormatting.RESET, username);
    }
  }

  /**
   * Check/uncheck an item from the given list.
   *
   * @param sender   Player who ran the command.
   * @param args     The command arguments.
   * @param username Name of the user associated to the calculator.
   * @param list     The list.
   * @param check    Whether to check the item or not.
   */
  private void checkItem(final ICommandSender sender, final String[] args, final String username, ToDoList list, final boolean check)
      throws NumberInvalidException {
    int index = parseInt(args[0]) - 1;
    boolean deleted;
    try {
      deleted = list.setChecked(index, check);
    } catch (IndexOutOfBoundsException e) {
      throw new NumberInvalidException("commands.todo.error.out_of_bounds");
    }
    if (!list.isVisible()) {
      String key;
      if (deleted) {
        key = "commands.todo.feedback.item_checked_deleted";
      } else if (check) {
        key = "commands.todo.feedback.item_checked";
      } else {
        key = "commands.todo.feedback.item_unchecked";
      }
      notifyCommandListener(sender, this, key, index + 1, username);
    }
  }

  /**
   * Get the value of an option of the given list.
   *
   * @param sender Player who ran the command.
   * @param args   The command arguments.
   * @param list   The list.
   */
  private void getListOption(final ICommandSender sender, final String[] args, ToDoList list)
      throws WrongUsageException {
    ListOption option = ListOption.getOption(args[0]).orElseThrow(() -> new WrongUsageException("commands.todo.usage"));
    notifyCommandListener(sender, this, option.getValue(list));
  }

  /**
   * Set an option of the given list.
   *
   * @param sender Player who ran the command.
   * @param args   The command arguments.
   * @param list   The list.
   */
  private void setListOption(final ICommandSender sender, final String[] args, ToDoList list)
      throws CommandException {
    ListOption option = ListOption.getOption(args[0]).orElseThrow(() -> new WrongUsageException("commands.todo.usage"));
    String rawValue = args[1];
    option.setValue(list, rawValue);
    notifyCommandListener(sender, this, "commands.todo.feedback.option_set", option, rawValue);
  }

  /**
   * Enumeration of available command options.
   */
  private enum Option {
    CLEAR, ADD, REMOVE, EDIT, CHECK, UNCHECK, GET, SET;

    /**
     * Return option’s name.
     */
    public String getName() {
      return this.name().toLowerCase();
    }

    @Override
    public String toString() {
      return this.getName();
    }

    /**
     * Return the option with the given name.
     *
     * @param name Option name.
     * @return The option.
     */
    public static Optional<Option> getOption(final String name) {
      for (Option option : values()) {
        if (option.getName().equals(name)) {
          return Optional.of(option);
        }
      }
      return Optional.empty();
    }
  }

  /**
   * Enumeration of available list options.
   */
  private enum ListOption {
    AUTO_DELETE_CHECKED(
        "autoDeleteChecked",
        list -> list.isAutoDeleteChecked() ? "true" : "false",
        Arrays.asList("true", "false")
    ),
    VISIBLE(
        "visible",
        list -> list.isVisible() ? "true" : "false",
        Arrays.asList("true", "false")
    );

    private final String name;
    private final Function<ToDoList, String> getter;
    private final List<String> suggestedValues;

    ListOption(final String name, final Function<ToDoList, String> getter, final List<String> suggestedValues) {
      this.name = name;
      this.getter = getter;
      this.suggestedValues = suggestedValues;
    }

    /**
     * Return the name of this option as used in the command.
     */
    public String getName() {
      return this.name;
    }

    /**
     * Return the value of this option for the given list.
     *
     * @param list The list to get the option value from.
     */
    public String getValue(final ToDoList list) {
      return this.getter.apply(list);
    }

    /**
     * Set the value of an option for the given list.
     *
     * @param list     The list to set the option of.
     * @param rawValue Raw value to be parsed.
     * @throws CommandException If the raw value could not be parsed.
     */
    public void setValue(ToDoList list, String rawValue) throws CommandException {
      // Cannot use lambdas because of CommandExceptions
      switch (this) {
        case AUTO_DELETE_CHECKED:
          list.setAutoDeleteChecked(parseBoolean(rawValue));
          break;
        case VISIBLE:
          list.setVisible(parseBoolean(rawValue));
          break;
      }
    }

    /**
     * Return a list of suggested values the option may accept.
     * May be empty.
     */
    public List<String> getSuggestedValues() {
      return this.suggestedValues;
    }

    @Override
    public String toString() {
      return this.getName();
    }

    /**
     * Return the list option with the given name.
     *
     * @param name Option name.
     * @return The option.
     */
    public static Optional<ListOption> getOption(final String name) {
      for (ListOption option : values()) {
        if (option.getName().equals(name)) {
          return Optional.of(option);
        }
      }
      return Optional.empty();
    }
  }
}
